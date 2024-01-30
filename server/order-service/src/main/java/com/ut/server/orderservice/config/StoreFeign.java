package com.ut.server.orderservice.config;

import com.ut.server.orderservice.dto.StoreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.ut.server.common.dtos.GenericResponseDTO;

import java.util.UUID;

@FeignClient(name = "store-service", url = "http://localhost:8091", path = "/api/v1/stores")
public interface StoreFeign {
    @GetMapping("/{storeId}")
    public GenericResponseDTO<StoreDto> getStoreById(
            @PathVariable("storeId") Long storeId,
            @RequestHeader("userId") UUID userId
    );

    @PostMapping("/create")
    public GenericResponseDTO<StoreDto> addNewStore(
            @RequestBody StoreDto newStore,
            @RequestHeader("userId") UUID userId
    );
}
