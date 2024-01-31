package org.ut.server.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.ut.server.common.constants.MessageConstant;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.common.exception.MessageCode;
import org.ut.server.userservice.dto.StoreDto;
import org.ut.server.userservice.service.StoreService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;

    @GetMapping("")
    public GenericResponseDTO<List<StoreDto>> getAllStores(@RequestHeader("userId") UUID userId) {
        try {
            List<StoreDto> storeDtos = storeService.getAllStores(userId);
            return GenericResponseDTO.<List<StoreDto>>builder()
                    .data(storeDtos)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_STORES)
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
            @RequestHeader("userId") UUID userId
    ) {
        try {
            StoreDto storeDto = storeService.getStoreById(userId, storeId);
            return GenericResponseDTO.<StoreDto>builder()
                    .data(storeDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_STORES)
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
    public GenericResponseDTO<StoreDto> addNewStore(
            @RequestBody StoreDto newStore,
            @RequestHeader("userId") UUID userId
    ) {
        try {
            StoreDto storeDto = storeService.addNewStore(newStore, userId);
            return GenericResponseDTO.<StoreDto>builder()
                    .data(storeDto)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_ORDER_CREATED)
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
    public GenericResponseDTO<String> deleteStoreById(@PathVariable("storeId") Long storeId, @RequestHeader("userId") UUID userId) {
        try {
            storeService.deleteStoreById(storeId, userId);
            return GenericResponseDTO.<String>builder()
                    .data(null)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_STORE_DELETED)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
