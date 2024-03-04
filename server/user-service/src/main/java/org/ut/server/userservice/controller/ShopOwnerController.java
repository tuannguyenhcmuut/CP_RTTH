package org.ut.server.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.userservice.common.MessageCode;
import org.ut.server.userservice.common.MessageConstants;
import org.ut.server.userservice.config.JwtUtils;
import org.ut.server.userservice.dto.FileDto;
import org.ut.server.userservice.dto.request.UserRequestDTO;
import org.ut.server.userservice.dto.response.GenericResponseDTO;
import org.ut.server.userservice.dto.response.UserResponseDTO;
import org.ut.server.userservice.model.Address;
import org.ut.server.userservice.service.IImageService;
import org.ut.server.userservice.service.ShopOwnerService;
import org.ut.server.userservice.utils.FileUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class ShopOwnerController {
//    private final ShopOwnerService shopOwnerService;

    private final ShopOwnerService shopOwnerService;

    private final JwtUtils jwtUtils;

//    @GetMapping("")
//    public ResponseEntity<List<User>> getUsers() {
//        return userService.getAllUser();
//    }

    @GetMapping("/{userId}")
    public GenericResponseDTO<UserResponseDTO> getUserInfo(@PathVariable UUID userId) {
        UserResponseDTO user =  shopOwnerService.getUserInfo(userId);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(user)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_USER)
                .timestamps(new Date())
                .build();
    }

    @GetMapping("/username/{username}/id")
    public GenericResponseDTO<UUID> getUserIdByUsername(@PathVariable String username) {
        UUID userId = shopOwnerService.getUserIdByUsername(username);
        return GenericResponseDTO.<UUID>builder()
                .data(userId)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_USER)
                .timestamps(new Date())
                .build();
    }

//    @GetMapping("/address")
//    public GenericResponseDTO<List<Address>> getAddress() {
//        List<Address> addresses = userService.getAllAddress();
//        return GenericResponseDTO.<List<Address>>builder()
//                .data(addresses)
//                .code(MessageCode.SUCCESS.toString())
//                .message(MessageConstants.SUCCESS_GET_ADDRESS)
//                .timestamps(new Date())
//                .build();
//    }

    @PostMapping("/create")
    public GenericResponseDTO<UserResponseDTO> createUser(@RequestBody UserRequestDTO newUser) throws SQLException {
        UserResponseDTO newUserRes =  shopOwnerService.createNewUser(newUser);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(newUserRes)
                .code(MessageCode.CREATED_SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_CREATED)
                .timestamps(new Date())
                .build();
    }

    @PatchMapping("/{userId}/update")
    public GenericResponseDTO<UserResponseDTO> updateUser(@PathVariable UUID userId, @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = shopOwnerService.updateUser(userId, userRequestDTO);
        return GenericResponseDTO.<UserResponseDTO>builder()
                .data(userResponseDTO)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_UPDATED)
                .timestamps(new Date())
                .build();
    }


//    @DeleteMapping("/{userId}")
    public GenericResponseDTO<String> deleteUserById(@PathVariable UUID userId) {
        shopOwnerService.deleteUserById(userId);
        return GenericResponseDTO.<String>builder()
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_USER_DELETED)
                .timestamps(new Date())
                .build();
    }

    @PostMapping("/image")
    public ResponseEntity<FileDto> uploadImage(@RequestParam("file") MultipartFile imageFile) {
        try {
            FileDto photo = shopOwnerService.uploadImage(imageFile);
            return ResponseEntity.ok(photo);
        }
        catch (Exception e) {
            log.error("Image upload error: ",e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/firebase/image")
    public ResponseEntity<FileDto> uploadImageToFirebase(@RequestParam("file") MultipartFile imageFile) throws IOException {
//        try {
           FileDto photo = shopOwnerService.uploadImage(imageFile);
            return ResponseEntity.ok(photo);
//        }
//        catch (Exception e) {
//            log.error("Image upload error: ",e.getMessage());
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
    }

    @PostMapping("/{userId}/avatar")
    public GenericResponseDTO<UserResponseDTO> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        UserResponseDTO userResponseDTO = shopOwnerService.uploadAvatar(file, userId);
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
        Address newAddress =  shopOwnerService.addAddressForUserById(userId, address);

        return GenericResponseDTO.<Address>builder()
                .data(newAddress)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_ADDRESS_CREATED)
                .timestamps(new Date())
                .build();
    }



    @DeleteMapping("{userId}/address/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable UUID userId,@PathVariable Long addressId) {
        shopOwnerService.deleteAddressById(userId, addressId);
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
