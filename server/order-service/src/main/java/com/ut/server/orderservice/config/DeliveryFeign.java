package com.ut.server.orderservice.config;

import com.ut.server.orderservice.dto.DeliveryDto;
import com.ut.server.orderservice.dto.DeliveryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.ut.server.common.dtos.GenericResponseDTO;

@FeignClient(name = "delivery-service", url = "http://localhost:8081", path = "/api/v1/delivery")
public interface DeliveryFeign {

    @PostMapping("")
    public GenericResponseDTO<DeliveryDto> createDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @RequestHeader("orderId") Long orderId);
}
