package org.ut.server.omsserver.exception;

public class ProductInUsedException extends RuntimeException{
    public ProductInUsedException(String message) {
        super(message);
    }
}
