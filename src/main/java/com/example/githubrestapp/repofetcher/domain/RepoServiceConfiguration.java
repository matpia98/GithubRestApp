package com.example.githubrestapp.repofetcher.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RepoServiceConfiguration {

    @Bean
    RepoService repoService(RepoFetchable repoFetchable) {
        return new RepoService(repoFetchable);
    }

}
