package com.example.githubrestapp.repofetcher.domain;

import com.example.githubrestapp.repofetcher.infrastructure.http.Branch;
import com.example.githubrestapp.repofetcher.infrastructure.http.Repos;

import java.util.List;

public interface RepoFetchable {
    List<Repos> fetchAllRepos(String username);
    List<Branch> fetchBranches(String owner, String repo);
}
