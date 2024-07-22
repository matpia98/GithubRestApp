package com.example.githubrestapp.repofetcher.controller;

import com.example.githubrestapp.repofetcher.infrastructure.controller.dto.GetRepositoriesBranches;
import com.example.githubrestapp.repofetcher.domain.UserNotFoundException;
import com.example.githubrestapp.repofetcher.infrastructure.handlers.error.dto.ErrorUserResponseDto;
import com.example.githubrestapp.repofetcher.infrastructure.http.Branch;
import com.example.githubrestapp.repofetcher.infrastructure.http.Commit;
import com.example.githubrestapp.repofetcher.domain.RepoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GithubRestAppControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RepoService repoService;

    @Test
    public void should_return_all_repos_for_user() throws Exception {
        // given
        String username = "testuser";
        List<GetRepositoriesBranches> repos = List.of(
                new GetRepositoriesBranches("repo1", "testuser", List.of(new Branch("main", new Commit("sha123")))),
                new GetRepositoriesBranches("repo2", "testuser", List.of(new Branch("develop", new Commit("sha456"))))
        );
        when(repoService.getAllReposWithBranches(username)).thenReturn(repos);

        // when, then
        MvcResult getAllReposMvcResult = mockMvc.perform(get("/user/" + username))
                .andExpect(status().isOk()).andReturn();
        String getAllReposJson = getAllReposMvcResult.getResponse().getContentAsString();
        List<GetRepositoriesBranches> getRepositoriesBranches = objectMapper.readValue(getAllReposJson, new TypeReference<>() {});
        assertThat(getRepositoriesBranches).containsExactlyInAnyOrder(
                repos.get(0), repos.get(1)
        );
    }

    @Test
    public void should_return_not_found_for_not_existent_user() throws Exception {
        // given
        String username = "notexistentuser";
        when(repoService.getAllReposWithBranches(username)).thenThrow(new UserNotFoundException("User not found"));

        // when, then
        MvcResult userNotFoundMvcResult = mockMvc.perform(get("/user/" + username))
                .andExpect(status().isNotFound()).andReturn();
        String userNotFoundJson = userNotFoundMvcResult.getResponse().getContentAsString();
        ErrorUserResponseDto errorUserResponseDto = objectMapper.readValue(userNotFoundJson, ErrorUserResponseDto.class);
        assertThat(errorUserResponseDto.status()).isEqualTo(404);
        assertThat(errorUserResponseDto.message()).isEqualTo("User not found");
    }



}
