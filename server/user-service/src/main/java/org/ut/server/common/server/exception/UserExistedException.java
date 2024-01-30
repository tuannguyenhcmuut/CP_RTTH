package org.ut.server.common.server.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class UserExistedException extends RuntimeException {
    public UserExistedException(String message) {
        super(message);

    }
}
