package com.example.githubrestapp.controller;

import com.example.githubrestapp.controller.dto.GetRepositoriesBranches;
import com.example.githubrestapp.service.RepoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class GithubRestAppController {

    private final RepoService service;

    @GetMapping(value = "/user/{username}")
    public ResponseEntity<List<GetRepositoriesBranches>> getAllReposFromUser(@PathVariable("username") String username) {
        List<GetRepositoriesBranches> allReposWithBranches = service.getAllReposWithBranches(username);
        return ResponseEntity.ok(allReposWithBranches);
    }


}
