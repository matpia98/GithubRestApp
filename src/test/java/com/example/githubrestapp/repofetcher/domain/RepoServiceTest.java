package com.example.githubrestapp.repofetcher.domain;

import com.example.githubrestapp.repofetcher.infrastructure.controller.dto.GetRepositoriesBranches;
import com.example.githubrestapp.repofetcher.infrastructure.http.Branch;
import com.example.githubrestapp.repofetcher.infrastructure.http.Commit;
import com.example.githubrestapp.repofetcher.infrastructure.http.Owner;
import com.example.githubrestapp.repofetcher.infrastructure.http.Repos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

public class RepoServiceTest {

    private RepoFetchable repoFetchable;
    private RepoService repoService;

    @BeforeEach
    public void setup() {
        repoFetchable = mock(RepoFetchable.class);
        repoService = new RepoService(repoFetchable);
    }

    @Test
    public void shouldReturnAllUserRepos() {
        // given
        String username = "testuser";
        List<Repos> repos = List.of(
                new Repos("repo1", new Owner(username), false),
                new Repos("repo2", new Owner(username), false)
        );

        List<Branch> branches = List.of(
                new Branch("main", new Commit("sha123")),
                new Branch("develop", new Commit("sha456"))
        );

        when(repoFetchable.fetchAllRepos(username)).thenReturn(repos);
        when(repoFetchable.fetchBranches(username, "repo1")).thenReturn(branches);
        when(repoFetchable.fetchBranches(username, "repo2")).thenReturn(branches);

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
        String username = "nonexistentuser";
        when(repoFetchable.fetchAllRepos(username)).thenThrow(new UserNotFoundException("User not found"));

        // when
        Throwable throwable = catchThrowable(() -> repoService.getAllReposWithBranches(username));

        // then
        assertThat(throwable).isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void shouldHandleRepositoryWithNoBranches() {
        // given
        String username = "testuser";
        List<Repos> repos = List.of(
                new Repos("repo1", new Owner(username), false)
        );

        when(repoFetchable.fetchAllRepos(username)).thenReturn(repos);
        when(repoFetchable.fetchBranches(username, "repo1")).thenReturn(Collections.emptyList());

        // when
        List<GetRepositoriesBranches> result = repoService.getAllReposWithBranches(username);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).extracting(GetRepositoriesBranches::name).containsExactly("repo1");
        assertThat(result).extracting(GetRepositoriesBranches::branches).containsExactly(Collections.emptyList());

    }

    @Test
    public void shouldHandleRepositoryWithOneBranch() {
        // given
        String username = "testuser";
        List<Repos> repos = List.of(
                new Repos("repo1", new Owner(username), false)
        );

        List<Branch> branches = List.of(
                new Branch("main", new Commit("sha123"))
        );

        when(repoFetchable.fetchAllRepos(username)).thenReturn(repos);
        when(repoFetchable.fetchBranches(username, "repo1")).thenReturn(branches);

        // when
        List<GetRepositoriesBranches> result = repoService.getAllReposWithBranches(username);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).extracting(GetRepositoriesBranches::name).containsExactly("repo1");
        assertThat(result).extracting(GetRepositoriesBranches::branches).hasSize(1);
        assertThat(result).extracting(GetRepositoriesBranches::branches).containsExactly(branches);
    }
}
