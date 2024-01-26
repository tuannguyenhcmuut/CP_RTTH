package com.ut.server.apigateway.exception;

public class GatewayException extends RuntimeException{
    public GatewayException(String message) {
        super(message);
    }
}
