package com.example.githubrestapp.repofetcher.infrastructure.http;

import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeignConfig {

    @Value("${github.api.token}")
    private String githubToken;

    @Bean
    public Decoder feignDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    // Uncomment the following lines to use the GitHub token for authentication
    //    @Bean
    //    public RequestInterceptor requestInterceptor() {
    //        return requestTemplate -> {
    //            if (!githubToken.isEmpty()) {
    //                requestTemplate.header("Authorization", "token " + githubToken);
    //            }
    //        };
    //    }

}
