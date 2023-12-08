package com.ut.server.apigateway.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface GatewayInterface {
    @GetMapping("validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader);
}
