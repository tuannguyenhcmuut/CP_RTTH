package org.ut.server.common.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.common.dtos.GenericResponseDTO;
import org.ut.server.common.exception.MessageCode;
import org.ut.server.common.server.common.MessageConstants;
import org.ut.server.common.server.dto.UserRequestDTO;
import org.ut.server.common.server.dto.UserResponseDTO;
import org.ut.server.common.server.model.Address;
import org.ut.server.common.server.service.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("")
//    public ResponseEntity<List<User>> getUsers() {
//        return userService.getAllUser();
//    }

    @GetMapping("/{userId}")
    public GenericResponseDTO<UserResponseDTO> getUserById(@PathVariable UUID userId) {
        UserResponseDTO user =  userService.getUserInfo(userId);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(user)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_USER)
                .timestamps(new Date())
                .build();
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAddress() {

        return userService.getAllAddress();
    }

    @PostMapping("/create")
    public GenericResponseDTO<UserResponseDTO> createUser(@RequestBody UserRequestDTO newUser) {
         UserResponseDTO newUserRes =  userService.createNewUser(newUser);
         return GenericResponseDTO.<UserResponseDTO>builder()
                 .data(newUserRes)
                 .code(MessageCode.CREATED_SUCCESS.toString())
                 .message(MessageConstants.SUCCESS_USER_CREATED)
                 .timestamps(new Date())
                 .build();
    }

    @PatchMapping("/update/{userId}")
    public GenericResponseDTO<UserResponseDTO> updateUser(@PathVariable UUID userId, @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.updateUser(userId, userRequestDTO);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(userResponseDTO)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_UPDATED)
                .timestamps(new Date())
                .build();
    }


    @DeleteMapping("/{userId}")
    public GenericResponseDTO<String> deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return GenericResponseDTO.<String>builder()
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_DELETED)
                .timestamps(new Date())
                .build();
    }

    @PostMapping("/avatar")
    public GenericResponseDTO<UserResponseDTO> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("userId") UUID userId) throws IOException {
        UserResponseDTO userResponseDTO = userService.uploadAvatar(file.getBytes(), userId);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(userResponseDTO)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_UPDATED)
                .timestamps(new Date())
                .build();
    }

    //Get list of addresses for user base on user_id
    @GetMapping("/address/{userId}")
    public ResponseEntity<List<Address>> getAddressForUserById(@PathVariable UUID userId) {
        return userService.getAddressForUserById(userId);
    }

    //Add new address for user base on user_id
    @PostMapping("/address/{userId}")
    public ResponseEntity<String> addAddressForUserById(@PathVariable UUID userId, @RequestBody Address address) {
        return userService.addAddressForUserById(userId, address);
    }



    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        return userService.deleteAddressById(addressId);
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
