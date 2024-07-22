package com.example.githubrestapp.repofetcher.domain;

import com.example.githubrestapp.repofetcher.infrastructure.controller.dto.GetRepositoriesBranches;
import com.example.githubrestapp.repofetcher.infrastructure.http.GithubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

public class RepoServiceTest {


    @Test
    public void shouldReturnAllUserRepos() {
        // given
        RepoService repoService = new RepoService(new GithubClientStub(false));
        String username = "testuser";

        // when
        List<GetRepositoriesBranches> result = repoService.getAllReposWithBranches(username);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(GetRepositoriesBranches::name)
                .containsExactlyInAnyOrder("repo1", "repo2");
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        // setup
        RepoService repoService = new RepoService(new GithubClientStub(true));
        String username = "nonexistentuser";

        // when
        Throwable throwable = catchThrowable(() -> repoService.getAllReposWithBranches(username));

        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }
}