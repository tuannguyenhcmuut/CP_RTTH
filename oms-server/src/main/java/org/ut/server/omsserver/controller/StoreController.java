package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.StoreDto;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.StoreService;
import org.ut.server.omsserver.utils.RestParamUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final JwtUtils jwtUtils;

    @GetMapping("")
    public GenericResponseDTO<List<StoreDto>> getAllStores(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            Pageable pageable = RestParamUtils.getPageable(page, size, sort);
            List<StoreDto> storeDtos = storeService.getAllStores(userId, pageable);
            return GenericResponseDTO.<List<StoreDto>>builder()
                    .data(storeDtos)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_STORES)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @GetMapping("/{storeId}")
    public GenericResponseDTO<StoreDto> getStoreById(
            @PathVariable("storeId") Long storeId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            StoreDto storeDto = storeService.getStoreById(userId, storeId);
            return GenericResponseDTO.<StoreDto>builder()
                    .data(storeDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_STORE)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    // add new store
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseDTO<StoreDto> addNewStore(
            @RequestBody StoreDto newStore,
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            StoreDto storeDto = storeService.addNewStore(newStore, userId);
            return GenericResponseDTO.<StoreDto>builder()
                    .data(storeDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_ORDER_CREATED)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

//    update store by id
    @PutMapping("/{storeId}")
    public GenericResponseDTO<StoreDto> updateStoreById(
            @PathVariable("storeId") Long storeId,
            @RequestBody StoreDto updatedStore,
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            StoreDto storeDto = storeService.updateStoreById(storeId, updatedStore, userId);
            return GenericResponseDTO.<StoreDto>builder()
                    .data(storeDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_STORE_UPDATED)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // delete store by id
    @DeleteMapping("/{storeId}")
    public GenericResponseDTO<String> deleteStoreById(
            @PathVariable("storeId") Long storeId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            storeService.deleteStoreById(storeId, userId);
            return GenericResponseDTO.<String>builder()
                    .data(null)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_STORE_DELETED)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // get all stores of owner
    @GetMapping("/owner/getall")
    public GenericResponseDTO<List<StoreDto>> getOwnerStores(
        @RequestHeader("Authorization") String token,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "name,asc") String[] sort
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        Pageable pageable = RestParamUtils.getPageable(page, size, sort);
        List<StoreDto> ownerStores = storeService.getOwnerStores(userId, pageable);
        return GenericResponseDTO.<List<StoreDto>>builder()
            .data(ownerStores)
            .code(MessageCode.SUCCESS.toString())
            .message(MessageConstants.SUCCESS_GET_OWNER_STORES)
            .timestamps(new Date())
            .build();
    }

    @GetMapping("statistics")
    public GenericResponseDTO<Long> getStoreStatistics(
        @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        Long storesCount = storeService.getTodayStores(userId);
        return GenericResponseDTO.<Long>builder()
                .data(storesCount)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_STORE_STATISTICS)
                .timestamps(new Date())
                .build();
    }
    

}
