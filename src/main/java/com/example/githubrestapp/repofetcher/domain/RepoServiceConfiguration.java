package com.example.githubrestapp.repofetcher.domain;

import com.example.githubrestapp.repofetcher.infrastructure.http.GithubClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RepoServiceConfiguration {

    @Bean
    RepoService repoService(GithubClient githubClient) {
        return new RepoService(githubClient);
    }

}
