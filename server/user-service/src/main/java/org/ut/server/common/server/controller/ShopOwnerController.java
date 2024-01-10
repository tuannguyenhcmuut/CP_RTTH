package org.ut.server.common.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ut.server.common.constants.MessageConstant;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.common.exception.MessageCode;
import org.ut.server.common.server.dto.ReceiverDto;
import org.ut.server.common.server.dto.ShopOwnerDto;
import org.ut.server.common.server.dto.UserRequestDTO;

import java.util.Date;
import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/suser")
//@Slf4j
//@RequiredArgsConstructor
public class ShopOwnerController {
//    private final ShopOwnerService shopOwnerService;

//    @PostMapping("/create")
//    public GenericResponseDTO<ShopOwnerDto> createShopUser(@RequestBody ShopOwnerDto newSUser) {
//        ShopOwnerDto newSUser = shopOwnerService.createNewSUser(newSUser);
//        return GenericResponseDTO<ShopOwnerDto>builder()
//                .data(newSUser)
//                .code(MessageCode.SUCCESS.toString())
//                .message(MessageConstant.SUCCESS_GET_ORDER)
//                .timestamps(new Date())
//                .build();
//    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createUser(@RequestBody ShopOwnerDto newSUser) {
//        return shopOwnerService.createNewSUser(newSUser);
//    }
}
