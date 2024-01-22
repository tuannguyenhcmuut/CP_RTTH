package org.ut.server.authservice.server.common.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ut.server.authservice.server.common.constants.MessageConstant;
import org.ut.server.authservice.server.common.dtos.GenericResponseDTO;
import org.ut.server.authservice.server.common.exception.MessageCode;
import org.ut.server.authservice.server.common.server.dto.ReceiverDto;
import org.ut.server.authservice.server.common.server.service.ReceiverService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/receivers")
//@Transactional
@Slf4j
public class ReceiverController {
    private final ReceiverService receiverService;

    public ReceiverController(ReceiverService receiverService) {
        this.receiverService = receiverService;
    }
    //Get list receiver of store
    @GetMapping("/user/{userId}")
    public GenericResponseDTO<List<ReceiverDto>> getReceiverByUserId(@PathVariable UUID userId) {
        try {
            List<ReceiverDto> receiverDtos = receiverService.getAllReceivers(userId);
            return GenericResponseDTO.<List<ReceiverDto>>builder()
                    .data(receiverDtos)
                    .code(MessageCode.SUCCESS.toString())
                    .message(MessageConstant.SUCCESS_GET_ORDER)
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

    //add new receiver
    @PostMapping("/create")
    public GenericResponseDTO<ReceiverDto> addReceiver(@RequestBody ReceiverDto newReceiver) {
//        try {
//        ReceiverDto receiverDto = receiverService.addNewReceiver(newReceiver, newReceiver.getShowOwnerId());
//            return GenericResponseDTO.<ReceiverDto>builder()
//                    .data(receiverDto)
//                    .code(MessageCode.SUCCESS.toString())
//                    .message(MessageConstant.SUCCESS_GET_ORDER)
//                    .timestamps(new Date())
//                    .build();
//
//        }
//        catch (Exception e) {
//            log.error(e.getMessage());
////            return GenericResponseDTO.<ReceiverDto>builder()
////                    .code(e.getMessage())
////                    .timestamps(new Date())
////                    .message(MessageConstant.UNSUCCESSFUL_GET_ORDER)
////                    .build();
//            throw new RuntimeException(e.getMessage());
//        }
        ReceiverDto receiverDto = receiverService.addNewReceiver(newReceiver, newReceiver.getUserId());
        return GenericResponseDTO.<ReceiverDto>builder()
                .data(receiverDto)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstant.SUCCESS_GET_ORDER)
                .timestamps(new Date())
                .build();
    }

    //delete receiver by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePersonById(@PathVariable Long id) {
        return  receiverService.deleteReceiverById(id);
    }

    //update receiver by id
//    @PutMapping("/{id}")
//    public ResponseEntity<String> updatePersonById(@PathVariable Long id, @RequestBody Receiver updatedReceiver) {
//        return receiverService.updateReceiverById(id, updatedReceiver);
//    }
}
