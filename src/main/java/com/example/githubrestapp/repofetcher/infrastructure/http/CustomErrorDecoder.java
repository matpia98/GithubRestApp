package com.example.githubrestapp.repofetcher.infrastructure.http;

import com.example.githubrestapp.repofetcher.domain.UserNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new UserNotFoundException("User not found");
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
