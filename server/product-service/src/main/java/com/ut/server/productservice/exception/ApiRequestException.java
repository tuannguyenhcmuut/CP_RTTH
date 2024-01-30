package com.ut.server.productservice.exception;

public class ApiRequestException extends RuntimeException{
    public ApiRequestException(String message) {
        super(message);
    }

}
