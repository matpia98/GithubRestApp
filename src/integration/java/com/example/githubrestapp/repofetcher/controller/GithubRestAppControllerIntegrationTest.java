package com.example.githubrestapp.repofetcher.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;


import com.example.githubrestapp.GithubRestAppApplication;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(classes = {GithubRestAppApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubRestAppControllerIntegrationTest {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.openfeign.client.config.github-client.url", wireMockServer::baseUrl);
    }

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }


    @Test
    public void shouldReturnAllReposForUser() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/users/testuser/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                                [
                                  {
                                    "name": "repo1",
                                    "owner": {
                                      "login": "testuser"
                                    },
                                    "fork": false
                                  },
                                  {
                                    "name": "repo2",
                                    "owner": {
                                      "login": "testuser"
                                    },
                                    "fork": true
                                  }
                                ]
                                """)));
        wireMockServer.stubFor(get(urlEqualTo("/repos/testuser/repo1/branches"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                                [
                                  {
                                    "name": "main",
                                    "commit": {
                                      "sha": "sha123"
                                    }
                                  },
                                  {
                                    "name": "develop",
                                    "commit": {
                                      "sha": "sha456"
                                    }
                                  }
                                ]
                                """)));

        // when, then
        when()
                .get("/user/testuser")
                .then()
                .statusCode(200)
                .body("[0].name", equalTo("repo1"))
                .body("[0].branches[0].name", equalTo("main"));

    }


    @Test
    public void shouldReturnNotFoundForNonexistentUser() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/users/notexistentuser/repos"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                  {
                                  "message": "Not Found",
                                  "documentation_url": "https://docs.github.com/rest/repos/repos#list-repositories-for-a-user",
                                  "status": "404"
                                  }
                                """)));

        // when, then
        when()
                .get("/user/notexistentuser")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("User not found"));
    }

    @Test
    public void shouldReturnForbiddenForRateLimitExceeded() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/users/testuser/repos"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                  {
                                  "message": "API rate limit exceeded",
                                  "documentation_url": "https://docs.github.com/rest/overview/resources-in-the-rest-api#rate-limiting"
                                  }
                                """)));

        // when, then
        when()
                .get("/user/testuser")
                .then()
                .statusCode(403)
                .body("message", equalTo("API rate limit exceeded"));
    }

    @Test
    public void shouldReturnEmptyListForUserWithOnlyForkedRepos() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/users/testuser/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("""
                                [
                                  {
                                    "name": "forkedrepo1",
                                    "owner": {
                                      "login": "testuser"
                                    },
                                    "fork": true
                                  },
                                  {
                                    "name": "forkedrepo2",
                                    "owner": {
                                      "login": "testuser"
                                    },
                                    "fork": true
                                  }
                                ]
                                """)));

        // when, then
        when()
                .get("/user/testuser")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    public void shouldReturnEmptyListForUserWithNoRepos() {
        // given
        wireMockServer.stubFor(get(urlEqualTo("/users/testuser/repos"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("[]")));

        // when, then
        when()
                .get("/user/testuser")
                .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }



}
