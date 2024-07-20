package com.example.githubrestapp.service;

import com.example.githubrestapp.http.apiresponses.Branch;

import java.util.List;

public record GetRepositoriesBranches(String name, String login, List<Branch> branches) {
}
