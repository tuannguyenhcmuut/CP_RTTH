package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.ReceiverDto;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.ReceiverService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/receivers")
//@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReceiverController {
    private final ReceiverService receiverService;
    private final JwtUtils jwtUtils;

    //Get list receiver of store
    @GetMapping("")
    public GenericResponseDTO<List<ReceiverDto>> getReceiverById(
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            List<ReceiverDto> receiverDtos = receiverService.getAllReceivers(userId);
            return GenericResponseDTO.<List<ReceiverDto>>builder()
                    .data(receiverDtos)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_RECEIVERS)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
//            return GenericResponseDTO.<List<ReceiverDto>>builder()
//                    .code(e.getMessage())
//                    .timestamps(new Date())
//                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
//                    .build();
            throw new RuntimeException(e.getMessage());
        }

    }

    @GetMapping("{receiverId}")
    public GenericResponseDTO<ReceiverDto> getReceiverById(
            @PathVariable Long receiverId ,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            ReceiverDto receiverDtos = receiverService.getReceiverById(receiverId, userId);
            return GenericResponseDTO.<ReceiverDto>builder()
                    .data(receiverDtos)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_RECEIVER)
                    .timestamps(new Date())
                    .build();


    }


    //add new receiver
    @PostMapping("/create")
    public GenericResponseDTO<ReceiverDto> addReceiver(
            @RequestBody ReceiverDto newReceiver,
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        ReceiverDto receiverDto = receiverService.addNewReceiver(newReceiver, userId);
        return GenericResponseDTO.<ReceiverDto>builder()
                .data(receiverDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_RECEIVER_CREATED)
                .timestamps(new Date())
                .build();
    }

    //delete receiver by id
    @DeleteMapping("/{receiverId}")
    public ResponseEntity<String> deleteReceiverById(
            @PathVariable Long receiverId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            receiverService.deleteReceiverById(receiverId, userId);
            return ResponseEntity.ok(MessageConstants.SUCCESS_RECEIVER_DELETED);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    //update receiver by id
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updatePersonById(@PathVariable Long id, @RequestBody Receiver updatedReceiver) {
//        return receiverService.updateReceiverById(id, updatedReceiver);
//    }

    @GetMapping("/owner/getall")
    public GenericResponseDTO<List<ReceiverDto>> getOwnerReceivers(
            @RequestHeader("Authorization") String token
    ) {
        try {
            UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
            List<ReceiverDto> receiverDtos = receiverService.getOwnerReceivers(userId);
            return GenericResponseDTO.<List<ReceiverDto>>builder()
                    .data(receiverDtos)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstants.SUCCESS_GET_OWNER_RECEIVERS)
                    .timestamps(new Date())
                    .build();
        }
        catch  (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
