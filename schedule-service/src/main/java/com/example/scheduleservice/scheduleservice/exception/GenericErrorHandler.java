package com.example.scheduleservice.scheduleservice.exception;

import com.example.scheduleservice.scheduleservice.exception.ExceptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GenericErrorHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler({ HttpClientErrorException.class })
    public ResponseEntity<ExceptionDTO> handleRestTemplateExceptions(HttpClientErrorException ex) throws IOException {
        ExceptionDTO errorDTO = objectMapper.readValue(ex.getResponseBodyAsString(), ExceptionDTO.class);
        return new ResponseEntity<>(errorDTO, ex.getResponseHeaders(),ex.getStatusCode());
    }

}
