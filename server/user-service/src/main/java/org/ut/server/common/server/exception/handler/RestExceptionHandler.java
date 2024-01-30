package org.ut.server.common.server.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.ut.server.common.exception.ErrorResponse;
import org.ut.server.common.server.exception.UserExistedException;
import org.ut.server.common.server.exception.UserNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(
//            RuntimeException e
//    ) {
//        // ... potential custom logic
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//
//        return new ResponseEntity<>(
//                new ErrorResponse(
//                        status,
//                        e.getMessage()
//                ),
//                status
//        );
//    }

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

        if (e instanceof UserExistedException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            e.getMessage(),
                            stackTrace
                    ),
                    status
            );
        }

        if (e instanceof UserNotFoundException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            e.getMessage(),
                            stackTrace
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
