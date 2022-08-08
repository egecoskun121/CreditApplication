package com.egecoskun.finalproject.exception.handler;


import com.egecoskun.finalproject.exception.ApplicantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GenericExceptionHandler {


    @ExceptionHandler(ApplicantNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleApplicantNotFoundException(ApplicantNotFoundException e){
        Map<String,String> errorResponseMap = new HashMap<>();
        errorResponseMap.put("error_message",e.getMessage());
        errorResponseMap.put("error_cause",e.getCause().toString());

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseMap);
    }
}
