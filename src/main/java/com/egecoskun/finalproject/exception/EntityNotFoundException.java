package com.egecoskun.finalproject.exception;

import lombok.Data;

@Data
public class  EntityNotFoundException extends RuntimeException {
    private String details;
    public EntityNotFoundException(String entityName, String cause) {
        super("Related " + entityName + " not found with : [" + cause + "]");

    }
}