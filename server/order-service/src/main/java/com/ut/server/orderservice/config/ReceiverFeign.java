package com.ut.server.orderservice.config;

import com.ut.server.orderservice.dto.ReceiverDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.ut.server.common.dtos.GenericResponseDTO;

import java.util.UUID;

@FeignClient(name = "receiver-service", url = "http://localhost:8091", path = "/api/v1/receivers")
public interface ReceiverFeign {
    @GetMapping("{receiverId}")
    public GenericResponseDTO<ReceiverDto> getReceiverById(
            @PathVariable Long receiverId ,
            @RequestHeader("userId") UUID userId
    );

    @PostMapping("/create")
    public GenericResponseDTO<ReceiverDto> addReceiver(
            @RequestBody ReceiverDto newReceiver,
            @RequestHeader("userId") UUID userId
    );

}

