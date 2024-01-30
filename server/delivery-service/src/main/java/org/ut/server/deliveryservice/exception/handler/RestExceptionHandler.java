package org.ut.server.deliveryservice.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.ut.server.common.exception.ErrorResponse;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(
            Exception e
    )
    {
        if (e instanceof NullPointerException) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.NOT_FOUND,
                            e.getMessage()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
