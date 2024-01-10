package com.ut.server.productservice.config;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "order-service", url = "http://localhost:8080/api/v1/orders")
public interface OrderFeign {
}
