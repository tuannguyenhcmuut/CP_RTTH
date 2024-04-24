package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.DeliveryDto;
import org.ut.server.omsserver.dto.request.DeliveryStatusRequest;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.DeliveryService;
import org.ut.server.omsserver.utils.RestParamUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final JwtUtils jwtUtils;

//    @PostMapping("")
//    @ResponseStatus(HttpStatus.CREATED)
//    public GenericResponseDTO<DeliveryDto> createDelivery(
//            @RequestBody DeliveryRequest deliveryRequest,
//            @RequestHeader("orderId") Long orderId
//    ) {
////        try {
//            DeliveryDto deliveryDto = deliveryService.createDelivery(deliveryRequest, orderId);
//            return GenericResponseDTO.<DeliveryDto>builder()
//                    .data(deliveryDto)
//                    .code("200")
//                    .message("Success")
//                    .timestamps(new Date())
//                    .build();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return GenericResponseDTO.<DeliveryDto>builder()
//                    .code(e.getMessage())
//                    .message("Failed")
//                    .timestamps(new Date())
//                    .build();
//        }
//    }

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

    /* SHIPPER */
    // get all order of shipper
    @GetMapping("/shipper")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    public GenericResponseDTO<List<DeliveryDto>> getAllDeliveryOfShipper(
            @RequestHeader("Authorization") String token,
            // paging sorting
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "lastUpdated,desc") String[] sort
    ) {
//        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            Pageable pageable  = RestParamUtils.getPageable(page, size, sort);
            List<DeliveryDto> deliveryDtos = deliveryService.getAllDeliveryOfShipper(userId, pageable);
            return GenericResponseDTO.<List<DeliveryDto>>builder()
                    .data(deliveryDtos)
                    .code("200")
                    .message("Success")
                    .timestamps(new Date())
                    .build();
    }

//    // phân shipper vào
//    @PutMapping("/{deliveryId}/shipper")
//    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('ROLE_SHIPPER')")
//    public GenericResponseDTO<DeliveryDto> assignShipperToDelivery(
//            @PathVariable Long deliveryId,
//            @RequestHeader("Authorization") String token,
//            // paging sorting
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size,
//            @RequestParam(defaultValue = "lastUpdated,desc") String[] sort
//
//    ) {
////        try {
//            UUID shipperId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
//            DeliveryDto deliveryDto = deliveryService.assignShipperToDelivery(deliveryId, shipperId, shipperId);
//            return GenericResponseDTO.<DeliveryDto>builder()
//                    .data(deliveryDto)
//                    .code("200")
//                    .message("Success")
//                    .timestamps(new Date())
//                    .build();
//    }
    // update status order
    @PatchMapping("/{deliveryId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_SHIPPER')")
    public GenericResponseDTO<DeliveryDto> updateStatusDelivery(
            @PathVariable Long deliveryId,
            @RequestHeader("Authorization") String token,
            @RequestBody DeliveryStatusRequest status
    ) {
//        try {
            UUID shipperId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            DeliveryDto deliveryDto = deliveryService.updateStatusDelivery(deliveryId, shipperId, status.getNewStatus());
            return GenericResponseDTO.<DeliveryDto>builder()
                    .data(deliveryDto)
                    .code("200")
                    .message("Order")
                    .timestamps(new Date())
                    .build();
    }

}
