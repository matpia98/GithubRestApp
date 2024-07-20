package com.example.githubrestapp.http.client;

import com.example.githubrestapp.http.apiresponses.Branch;
import com.example.githubrestapp.http.apiresponses.Repos;
import com.example.githubrestapp.http.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "github-client", configuration = FeignConfig.class)
public interface GithubClient {

    @GetMapping("/users/{username}/repos")
    List<Repos> fetchAllRepos(@PathVariable("username") String username);

    @GetMapping("/repos/{owner}/{repo}/branches")
    List<Branch> fetchBranches(@PathVariable("owner") String owner, @PathVariable("repo") String repo);
 }
