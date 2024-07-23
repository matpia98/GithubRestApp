package com.example.githubrestapp.repofetcher.infrastructure.http;

import com.example.githubrestapp.repofetcher.domain.RepoFetchable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "github-client", configuration = FeignConfig.class)
interface GithubClient extends RepoFetchable {

    @Override
    @GetMapping("/users/{username}/repos")
    List<Repos> fetchAllRepos(@PathVariable("username") String username);

    @Override
    @GetMapping("/repos/{owner}/{repo}/branches")
    List<Branch> fetchBranches(@PathVariable("owner") String owner, @PathVariable("repo") String repo);
 }
