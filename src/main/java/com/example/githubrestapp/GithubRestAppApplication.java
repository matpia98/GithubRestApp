package com.example.githubrestapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GithubRestAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubRestAppApplication.class, args);
    }

}
