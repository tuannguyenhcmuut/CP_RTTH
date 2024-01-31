package org.ut.server.userservice.exception;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message) {
        super(message);

    }
}
