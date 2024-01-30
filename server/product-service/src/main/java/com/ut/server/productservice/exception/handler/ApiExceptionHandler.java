package com.ut.server.productservice.exception.handler;

import com.ut.server.productservice.exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.ut.server.common.exception.ErrorResponse;

import java.time.ZonedDateTime;


@RestControllerAdvice
@Component
public class ApiExceptionHandler {
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiException(ApiRequestException ex) {
        // 1. create payload containing exception details
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse apiException = new ErrorResponse(
                status,
                ex.getMessage()
        );
        // 2. return response entity
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        if (cause instanceof ApiRequestException) {
            // handle your custom exception here
//            YourCustomException yourCustomException = (YourCustomException) cause;
            // create your error response
            ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    cause.getMessage()
            );
            // return your error response
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        // if it's not your custom exception, let the default exception handler handle it
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleApiException(Exception ex) {
        // 1. create payload containing exception details
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse apiException = new ErrorResponse(
                status,
                ex.getMessage()
        );
        // 2. return response entity] o.s.w.s.m.m.a.HttpEntityMethodProcessor  : Writing [ErrorResponse(code=400, status=BAD_REQUEST, message=Transaction silently rolled back because it has  (truncated)...]
        //2024-01-28 14:15:57.132 DEBUG 22988 --- [nio-8082-exec-1] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.transaction.UnexpectedRollbackException: Transaction silently rolled back because it has been marked as rollback-only]
        //2024-01-28 14:15:57.132 DEBUG 22988 --- [nio-8082-exec-1] o.
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
