package com.example.githubrestapp.repofetcher.domain;

import com.example.githubrestapp.repofetcher.infrastructure.http.Branch;
import com.example.githubrestapp.repofetcher.infrastructure.http.Commit;
import com.example.githubrestapp.repofetcher.infrastructure.http.Owner;
import com.example.githubrestapp.repofetcher.infrastructure.http.Repos;

import java.util.Arrays;
import java.util.List;

class GithubClientStub implements RepoFetchable {
    private final boolean simulateUserNotFound;

    public GithubClientStub(boolean simulateUserNotFound) {
        this.simulateUserNotFound = simulateUserNotFound;
    }

    @Override
    public List<Repos> fetchAllRepos(String username) {
        if (simulateUserNotFound) {
            throw new UserNotFoundException("User not found");
        }
        return Arrays.asList(
                new Repos("repo1", new Owner(username), false),
                new Repos("repo2", new Owner(username), false)
        );
    }

    @Override
    public List<Branch> fetchBranches(String owner, String repo) {
        return Arrays.asList(
                new Branch("main", new Commit("sha123")),
                new Branch("develop", new Commit("sha456"))
        );
    }
}
