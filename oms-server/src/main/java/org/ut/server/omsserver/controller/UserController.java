package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.FileDto;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.dto.response.UserResponseDTO;
import org.ut.server.omsserver.model.Address;
import org.ut.server.omsserver.service.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
//@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @GetMapping("")
//    public ResponseEntity<List<User>> getUsers() {
//        return userService.getAllUser();
//    }

//    @GetMapping("/{userId}")
//    public GenericResponseDTO<UserResponseDTO> getUserInfo(@PathVariable UUID userId) {
//        UserResponseDTO user =  userService.getUserInfo(userId);
//        return GenericResponseDTO.<UserResponseDTO>builder()
//                .data(user)
//                .code(MessageCode.SUCCESS.toString())
//                .message(MessageConstants.SUCCESS_GET_USER)
//                .timestamps(new Date())
//                .build();
//    }
//
//    @GetMapping("/username/{username}/id")
//    public GenericResponseDTO<UUID> getUserIdByUsername(@PathVariable String username) {
//        UUID userId = userService.getUserIdByUsername(username);
//        return GenericResponseDTO.<UUID>builder()
//                .data(userId)
//                .code(MessageCode.SUCCESS.toString())
//                .message(MessageConstants.SUCCESS_GET_USER)
//                .timestamps(new Date())
//                .build();
//    }
//
////    @GetMapping("/address")
////    public GenericResponseDTO<List<Address>> getAddress() {
////        List<Address> addresses = userService.getAllAddress();
////        return GenericResponseDTO.<List<Address>>builder()
////                .data(addresses)
////                .code(MessageCode.SUCCESS.toString())
////                .message(MessageConstants.SUCCESS_GET_ADDRESS)
////                .timestamps(new Date())
////                .build();
////    }
//
//    @PostMapping("/create")
//    public GenericResponseDTO<UserResponseDTO> createUser(@RequestBody UserRequestDTO newUser) {
//         UserResponseDTO newUserRes =  userService.createNewUser(newUser);
//         return GenericResponseDTO.<UserResponseDTO>builder()
//                 .data(newUserRes)
//                 .code(MessageCode.CREATED_SUCCESS.toString())
//                 .message(MessageConstants.SUCCESS_USER_CREATED)
//                 .timestamps(new Date())
//                 .build();
//    }

//    @PatchMapping("/{userId}/update")
//    public GenericResponseDTO<UserResponseDTO> updateUser(@PathVariable UUID userId, @RequestBody UserRequestDTO userRequestDTO) {
//        UserResponseDTO userResponseDTO = userService.updateUser(userId, userRequestDTO);
//        return GenericResponseDTO.<UserResponseDTO>builder()
//                .data(userResponseDTO)
//                .code(MessageCode.SUCCESS.toString())
//                .message(MessageConstants.SUCCESS_USER_UPDATED)
//                .timestamps(new Date())
//                .build();
//    }


    @DeleteMapping("/{userId}")
    public GenericResponseDTO<String> deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return GenericResponseDTO.<String>builder()
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_DELETED)
                .timestamps(new Date())
                .build();
    }

    @PostMapping("/image")
    public ResponseEntity<FileDto> uploadProductImage(@RequestParam("file") MultipartFile imageFile) {
        try {
            FileDto photo = userService.uploadImage(imageFile);
            return ResponseEntity.ok(photo);
        }
        catch (Exception e) {
            log.error("Image upload error: ",e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{userId}/avatar")
    public GenericResponseDTO<UserResponseDTO> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @PathVariable UUID userId
    ) throws IOException {
        UserResponseDTO userResponseDTO = userService.uploadAvatar(file, userId);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(userResponseDTO)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_UPDATED)
                .timestamps(new Date())
                .build();
    }

    //Get list of addresses for user base on user_id
//    @GetMapping("{userId}/address")
//    public GenericResponseDTO<List<Address>> getAddressForUserById(@PathVariable UUID userId) {
//        List<Address> addresses = userService.getAddressForUserById(userId);
//        return GenericResponseDTO.<List<Address>>builder()
//                .data(addresses)
//                .code(MessageCode.SUCCESS.toString())
//                .message(MessageConstants.SUCCESS_GET_ADDRESS)
//                .timestamps(new Date())
//                .build();
//    }

    //Add new address for user base on user_id
    @PostMapping("{userId}/address")
    public GenericResponseDTO<Address> addAddressForUserById(@PathVariable UUID userId, @RequestBody Address address) {
        Address newAddress =  userService.addAddressForUserById(userId, address);

        return GenericResponseDTO.<Address>builder()
                .data(newAddress)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ADDRESS_CREATED)
                .timestamps(new Date())
                .build();
    }



    @DeleteMapping("{userId}/address/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable UUID userId,@PathVariable Long addressId) {
        userService.deleteAddressById(userId, addressId);
        return ResponseEntity.ok("Delete address successfully!");
    }

    //update user by id

    //get list_receiver by user_id
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Receiver>> getReceiverOfUser(@PathVariable UUID userId) {
//        return userService.getReceiverOfUser(userId);
//    }

//    @PostMapping("/{user_id}/add")
//    public ResponseEntity<String> addReceiverOfUser(@PathVariable UUID user_id, @RequestBody Receiver newReceiver) {
//        return userService.addReceiverOfUser(user_id, newReceiver);
//    }
}
