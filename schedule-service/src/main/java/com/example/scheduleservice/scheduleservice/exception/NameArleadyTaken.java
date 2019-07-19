package com.example.scheduleservice.scheduleservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NameArleadyTaken extends RuntimeException {

    public NameArleadyTaken(String message){
        super(message);
    }
}


