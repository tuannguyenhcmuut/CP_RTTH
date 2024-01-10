package com.ut.server.orderservice.config;

import com.ut.server.orderservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.ut.server.common.dtos.GenericResponseDTO;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "product-service", url = "http://localhost:8082", path = "/api/v1/products")
public interface ProductFeign {
    @GetMapping
    public GenericResponseDTO<List<ProductResponse>> getAllProduct(@RequestParam UUID userId);
    @GetMapping
    public GenericResponseDTO<ProductResponse> getProduct(@PathVariable Long productId, @RequestParam UUID userId);
}
