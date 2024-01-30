package org.ut.server.common.server.exception;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message) {
        super(message);

    }
}
