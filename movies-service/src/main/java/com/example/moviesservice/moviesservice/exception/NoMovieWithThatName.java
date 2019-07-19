package com.example.moviesservice.moviesservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoMovieWithThatName extends RuntimeException {

    public NoMovieWithThatName(String message){
        super(message);
    }
}
