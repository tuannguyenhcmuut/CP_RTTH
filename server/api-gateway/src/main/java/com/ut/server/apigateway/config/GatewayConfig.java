package com.ut.server.apigateway.config;

import com.ut.server.apigateway.exception.handler.RestExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean("handlerExceptionResolver")
    public RestExceptionHandler handlerExceptionResolver() {
        RestExceptionHandler resolver = new RestExceptionHandler();
        // Configure the resolver here if needed
        return resolver;
    }
}
