package org.ut.server.deliveryservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.ut.server.common.dtos.GenericResponseDTO;

import java.util.UUID;

@FeignClient(name = "order-service", url = "http://localhost:8083", path = "/api/v1/order")
public interface OrderFeign {
    @GetMapping("/{orderId}")
    public GenericResponseDTO<?> getOrderById(
            @RequestHeader("userId") UUID userId,
            @PathVariable Long orderId
    );
}
