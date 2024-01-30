package com.ut.server.orderservice.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ut.server.orderservice.exception.OrderNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.ut.server.common.exception.ErrorResponse;
import org.ut.server.common.utils.JsonMapperUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleExceptions(
            Exception e
    ) throws JsonProcessingException {

        if (e instanceof NullPointerException) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.NOT_FOUND,
                            e.getMessage()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }

        // null pointer exception
        if (e instanceof NullPointerException) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            e.getMessage()
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        if (e instanceof FeignException) {
            log.error(e.getMessage());
            String responseBody = ((FeignException) e).contentUTF8();
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            JsonMapperUtils.toErrorResponseJson(responseBody).getMessage()
                    ),
                    status
            );
        }


        if (e instanceof OrderNotFoundException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            e.getMessage(),
                            stringWriter.toString()
                    ),
                    status
            );
        }



        if (e instanceof MethodArgumentTypeMismatchException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            e.getMessage(),
                            stringWriter.toString()
                    ),
                    status
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
