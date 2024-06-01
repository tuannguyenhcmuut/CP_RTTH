package org.ut.server.omsserver.exception;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message) {
        super(message);

    }
}
