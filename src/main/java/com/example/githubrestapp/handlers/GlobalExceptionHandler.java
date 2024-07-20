package com.example.githubrestapp.handlers;

import com.example.githubrestapp.exceptions.UserNotFoundException;
import com.example.githubrestapp.handlers.error.dto.ErrorResponseFormatDto;
import com.example.githubrestapp.handlers.error.dto.ErrorUserResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorUserResponseDto> handleUserNotFoundException(UserNotFoundException exception) {
        log.warn("UserNotFoundException while accessing the user");
        ErrorUserResponseDto errorUserResponseDto = new ErrorUserResponseDto(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorUserResponseDto);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseFormatDto> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        log.warn("HttpMediaTypeNotAcceptableException while trying to get xml response format: " + ex.getMessage());
        ErrorResponseFormatDto response = new ErrorResponseFormatDto(HttpStatus.NOT_ACCEPTABLE.value(), "XML format is not supported");
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
