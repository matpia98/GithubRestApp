package com.example.githubrestapp.controller.dto;

import com.example.githubrestapp.http.apiresponses.Branch;

import java.util.List;

public record GetRepositoriesBranches(String name, String login, List<Branch> branches) {
}
