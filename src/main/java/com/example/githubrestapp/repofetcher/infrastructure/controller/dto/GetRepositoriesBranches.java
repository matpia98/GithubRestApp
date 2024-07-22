package com.example.githubrestapp.repofetcher.infrastructure.controller.dto;

import com.example.githubrestapp.repofetcher.infrastructure.http.Branch;

import java.util.List;

public record GetRepositoriesBranches(String name, String login, List<Branch> branches) {
}
