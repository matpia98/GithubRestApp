package com.example.githubrestapp.service;

import com.example.githubrestapp.exceptions.UserNotFoundException;
import com.example.githubrestapp.http.apiresponses.Branch;
import com.example.githubrestapp.http.apiresponses.Repos;
import com.example.githubrestapp.http.client.GithubClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RepoService {

    private final GithubClient githubClient;

    public List<GetRepositoriesBranches> getAllReposWithBranches(String username) {
        List<Repos> allRepos = getAllRepos(username);
        return allRepos.stream()
                .map(repo -> {
                    List<Branch> branches = githubClient.fetchBranches(repo.owner().login(), repo.name());
                    return new GetRepositoriesBranches(repo.name(), repo.owner().login(), branches);
                })
                .collect(Collectors.toList());
    }

    private List<Repos> getAllRepos(String username) {
        List<Repos> repos = githubClient.fetchAllRepos(username);
        if (repos.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        return repos.stream()
                .filter(repo -> !repo.fork())
                .collect(Collectors.toList());
    }



}
