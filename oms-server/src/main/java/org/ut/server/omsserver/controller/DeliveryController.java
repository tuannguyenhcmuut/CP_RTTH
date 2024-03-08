package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.DeliveryDto;
import org.ut.server.omsserver.dto.request.DeliveryRequest;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.DeliveryService;

import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final JwtUtils jwtUtils;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<DeliveryDto> createDelivery(
            @RequestBody DeliveryRequest deliveryRequest,
            @RequestHeader("orderId") Long orderId
    ) {
//        try {
            DeliveryDto deliveryDto = deliveryService.createDelivery(deliveryRequest, orderId);
            return GenericResponseDTO.<DeliveryDto>builder()
                    .data(deliveryDto)
                    .code("200")
                    .message("Success")
                    .timestamps(new Date())
                    .build();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return GenericResponseDTO.<DeliveryDto>builder()
//                    .code(e.getMessage())
//                    .message("Failed")
//                    .timestamps(new Date())
//                    .build();
//        }
    }

    @GetMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<DeliveryDto> getDeliveryById(
            @PathVariable Long deliveryId,
            @RequestHeader("Authorization") String token
    ) {
//        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            DeliveryDto deliveryDto = deliveryService.getDeliveryById(deliveryId, userId);
            return GenericResponseDTO.<DeliveryDto>builder()
                    .data(deliveryDto)
                    .code("200")
                    .message("Success")
                    .timestamps(new Date())
                    .build();
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                return GenericResponseDTO.<DeliveryDto>builder()
//                        .code("404")
//                        .message(e.getMessage())
//                        .timestamps(new Date())
//                        .build();
//        }
    }
}
