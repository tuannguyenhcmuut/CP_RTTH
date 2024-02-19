package org.ut.server.userservice.exception;

public class ProductInUsedException extends RuntimeException{
    public ProductInUsedException(String message) {
        super(message);
    }
}
