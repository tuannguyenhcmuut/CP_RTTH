package org.ut.server.userservice.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.ut.server.userservice.exception.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

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

    // user not found
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

    // SQLException
//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity<Object> handleSQLException(SQLException ex) {
//        // 1. create payload containing exception details
//        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
//        ErrorResponse apiException = new ErrorResponse(
//                status,
//                ex.getMessage()
//        );
//        log.error("SQL Exception Error: " + ex.getSQLState());
//        // 2. return response entity
//        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    IOException
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ex) {
        // 1. create payload containing exception details
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse apiException = new ErrorResponse(
                status,
                ex.getMessage()
        );
        // 2. return response entity
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
//    java.lang.IllegalArgumentException:
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 1. create payload containing exception details
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse apiException = new ErrorResponse(
                status,
                ex.getMessage()
        );
        // 2. return response entity
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
//JsonProcessingException
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
        // 1. create payload containing exception details
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse apiException = new ErrorResponse(
                status,
                ex.getMessage()
        );
        // 2. return response entity
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // FileUploadException
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<Object> handleFileUploadException(FileUploadException ex) {
        // 1. create payload containing exception details
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse apiException = new ErrorResponse(
                status,
                ex.getMessage()
        );
        // 2. return response entity
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

//    SQLException
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLExceptions(
            SQLException e
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage()
                ),
                status
        );
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

        ErrorResponse errorDetail = new ErrorResponse();

        // TokenRefreshException
        if (e instanceof TokenRefreshException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            e.getMessage()
                    ),
                    status
            );
        }

        if (e instanceof ExceptionHandler) {
            HttpStatus status = HttpStatus.UNAUTHORIZED; // 401

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
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

        if (e instanceof AccessDeniedException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
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

        if (e instanceof SignatureException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
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

        if (e instanceof ExpiredJwtException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
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

        if (e instanceof MalformedJwtException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
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
        if (e instanceof BadCredentialsException) {
            HttpStatus status = HttpStatus.FORBIDDEN; // 403

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
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

//        HttpMessageNotReadableException
        if (e instanceof HttpMessageNotReadableException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;

            // converting the stack trace to a String
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTrace = stringWriter.toString();

            return new ResponseEntity<>(
                    new ErrorResponse(
                            status,
                            "Invalid value for user information",
                            stackTrace
                    ),
                    status
            );
        }
        // FileUploadException
        if (e instanceof FileUploadException) {
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

        //UserException
        if (e instanceof UserException) {
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

        // address
        if (e instanceof AddressException) {
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

        // HttpClientErrorException
        if (e instanceof HttpClientErrorException) {
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

        // store not found
        if (e instanceof StoreNotFoundException) {
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
        // receiver not found
        if (e instanceof ReceiverNotFoundException) {
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

        // order not found
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

        // delivery not found
        if (e instanceof DeliveryNotFoundException) {
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

        // product not found
        if (e instanceof ProductNotFoundException) {
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

        //ProductInUsedException
        if (e instanceof ProductInUsedException) {
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
