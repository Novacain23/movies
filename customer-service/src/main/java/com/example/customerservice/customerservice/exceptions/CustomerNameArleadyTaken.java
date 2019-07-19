package com.example.customerservice.customerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerNameArleadyTaken extends RuntimeException {

    public CustomerNameArleadyTaken(String message){
        super(message);
    }
}


