package org.ut.server.authservice.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
@Component
public class RestExceptionHandler {
    @ExceptionHandler(NullPointerException.class) // exception handled
    public ResponseEntity<ErrorResponse> handleNullPointerExceptions(
            Exception e
    ) {
        // ... potential custom logic

        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage()
                ),
                status
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorDetail = new ErrorResponse();
        if (ex instanceof ExceptionHandler) {
            HttpStatus status = HttpStatus.UNAUTHORIZED; // 401

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "UNAUTHORIZED: Authentication Failure",
                            stackTrace
                    ),
                    status
            );
        }

        if (ex instanceof AccessDeniedException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "ACCESS_DEFINED: not authorized!!",
                            stackTrace
                    ),
                    status
            );
        }

        if (ex instanceof SignatureException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "JWT Signature not valid!!",
                            stackTrace
                    ),
                    status
            );
        }

        if (ex instanceof ExpiredJwtException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "JWT token already expired!!",
                            stackTrace
                    ),
                    status
            );
        }

        if (ex instanceof MalformedJwtException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "Illegal token format!",
                            stackTrace
                    ),
                    status
            );
        }
        // BadCredentialsException
        if (ex instanceof BadCredentialsException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "Username or password is incorrect!",
                            stackTrace
                    ),
                    status
            );
        }

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        ex.getStackTrace().toString()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
