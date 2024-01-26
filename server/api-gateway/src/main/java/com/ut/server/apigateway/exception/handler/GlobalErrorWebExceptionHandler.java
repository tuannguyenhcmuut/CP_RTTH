package com.ut.server.apigateway.exception.handler;

import com.ut.server.apigateway.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@Component
//@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer
    ) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        setMessageWriters(serverCodecConfigurer.getWriters());
        setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> renderErrorResponse(request));
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        Throwable error = getError(request);

        if (error instanceof AccessDeniedException) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                    .bodyValue(
                            new ErrorResponse(
                                    HttpStatus.FORBIDDEN,
                                    "ACCESS_DENIED: not authorized!!",
                                    error.getStackTrace().toString()
                            )
                    );
        }

        if (error instanceof SignatureException) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                    .bodyValue(
                            new ErrorResponse(
                                    HttpStatus.FORBIDDEN,
                                    "JWT Signature not valid!!",
                                    error.getStackTrace().toString()
                            )
                    );
        }

        if (error instanceof ExpiredJwtException) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                    .bodyValue(
                            new ErrorResponse(
                                    HttpStatus.FORBIDDEN,
                                    "JWT token already expired!!",
                                    error.getStackTrace().toString()
                            )
                    );
        }

        if (error instanceof MalformedJwtException) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                    .bodyValue(
                            new ErrorResponse(
                                    HttpStatus.FORBIDDEN,
                                    "Illegal token format!",
                                    error.getStackTrace().toString()
                            )
                    );
        }


        // Build and return ErrorResponse object
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                error.getMessage(),
                error.getStackTrace().toString()
        );

        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorResponse));
    }


}


