package com.example.githubrestapp.repofetcher.domain;

import com.example.githubrestapp.repofetcher.infrastructure.controller.dto.GetRepositoriesBranches;
import com.example.githubrestapp.repofetcher.infrastructure.http.Branch;
import com.example.githubrestapp.repofetcher.infrastructure.http.Repos;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RepoService {

    private final RepoFetchable repoFetchable;

    public List<GetRepositoriesBranches> getAllReposWithBranches(String username) {
        List<Repos> allRepos = getAllRepos(username);
        return allRepos.stream()
                .map(repo -> {
                    List<Branch> branches = repoFetchable.fetchBranches(repo.owner().login(), repo.name());
                    return new GetRepositoriesBranches(repo.name(), repo.owner().login(), branches);
                })
                .collect(Collectors.toList());
    }

    private List<Repos> getAllRepos(String username) {
        List<Repos> repos = repoFetchable.fetchAllRepos(username);
        return repos.stream()
                .filter(repo -> !repo.fork())
                .collect(Collectors.toList());
    }



}
