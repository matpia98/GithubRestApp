package com.example.githubrestapp.repofetcher.infrastructure.handlers.error.dto;

public record ErrorApiLimitExceededDto(Integer status, String message) {
}
