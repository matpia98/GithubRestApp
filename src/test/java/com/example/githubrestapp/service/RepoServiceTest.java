package com.example.githubrestapp.service;

import com.example.githubrestapp.controller.dto.GetRepositoriesBranches;
import com.example.githubrestapp.http.apiresponses.Branch;
import com.example.githubrestapp.http.apiresponses.Commit;
import com.example.githubrestapp.http.apiresponses.Owner;
import com.example.githubrestapp.http.apiresponses.Repos;
import com.example.githubrestapp.http.client.GithubClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepoServiceTest {

    @Mock
    private GithubClient githubClient = mock(GithubClient.class);

    @InjectMocks
    private RepoService repoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllUserRepos() {
        // given
        String username = "testuser";
        List<Repos> repos = List.of(
                new Repos("repo1", new Owner("testuser"), false),
                new Repos("repo2", new Owner("testuser"), false)
        );
        List<Branch> branchesRepo1 = List.of(new Branch("main", new Commit("sha123")));
        List<Branch> branchesRepo2 = List.of(new Branch("develop", new Commit("sha456")));
        when(githubClient.fetchAllRepos(username)).thenReturn(repos);
        when(githubClient.fetchBranches("testuser", "repo1")).thenReturn(branchesRepo1);
        when(githubClient.fetchBranches("testuser", "repo2")).thenReturn(branchesRepo2);

        // when
        List<GetRepositoriesBranches> result = repoService.getAllReposWithBranches(username);


        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(GetRepositoriesBranches::name)
                .containsExactlyInAnyOrder("repo1", "repo2");
        assertThat(result).extracting(GetRepositoriesBranches::branches)
                        .containsExactlyInAnyOrder(branchesRepo1, branchesRepo2);

        // then
        verify(githubClient, times(1)).fetchAllRepos(username);
        verify(githubClient, times(1)).fetchBranches("testuser", "repo1");
        verify(githubClient, times(1)).fetchBranches("testuser", "repo2");
    }

}
