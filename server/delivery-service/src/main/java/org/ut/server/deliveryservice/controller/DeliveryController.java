package org.ut.server.deliveryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.deliveryservice.dto.DeliveryDto;
import org.ut.server.deliveryservice.dto.DeliveryRequest;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.deliveryservice.service.DeliveryService;

import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<DeliveryDto> createDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @RequestHeader("orderId") Long orderId
    ) {
        try {
            DeliveryDto deliveryDto = deliveryService.createDelivery(deliveryRequest, orderId);
            return GenericResponseDTO.<DeliveryDto>builder()
                    .data(deliveryDto)
                    .code("200")
                    .message("Success")
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponseDTO.<DeliveryDto>builder()
                    .code(e.getMessage())
                    .message("Failed")
                    .timestamps(new Date())
                    .build();
        }
    }

    @GetMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<DeliveryDto> getDeliveryById(
            @PathVariable Long deliveryId,
            @RequestHeader("userId") UUID userId
    ) {
        try {
            DeliveryDto deliveryDto = deliveryService.getDeliveryById(deliveryId, userId);
            return GenericResponseDTO.<DeliveryDto>builder()
                    .data(deliveryDto)
                    .code("200")
                    .message("Success")
                    .timestamps(new Date())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return GenericResponseDTO.<DeliveryDto>builder()
                    .code(e.getMessage())
                    .message("Failed")
                    .timestamps(new Date())
                    .build();
        }
    }
}
