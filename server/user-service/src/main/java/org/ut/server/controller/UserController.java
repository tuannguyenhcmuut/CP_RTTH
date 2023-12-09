package org.ut.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ut.server.dto.UserRequestDTO;
import org.ut.server.model.Address;
import org.ut.server.model.Receiver;
import org.ut.server.model.User;
import org.ut.server.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID user_id) {
        return userService.getUserInfo(user_id);
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAddress() {

        return userService.getAllAddress();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO newUser) {
        return userService.createNewUser(newUser);
    }

    //Get list of addresses for user base on user_id
    @GetMapping("/address/{id}")
    public ResponseEntity<List<Address>> getAddressForUserById(@PathVariable UUID id) {
        return userService.getAddressForUserById(id);
    }

    //Add new address for user base on user_id
    @PostMapping("/address/{id}")
    public ResponseEntity<String> addAddressForUserById(@PathVariable UUID id, @RequestBody Address address) {
        return userService.addAddressForUserById(id, address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        return userService.deleteUserById(id);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long addressId) {
        return userService.deleteAddressById(addressId);
    }

    //update user by id

    //get list_receiver by user_id
    @GetMapping("/{userId}")
    public ResponseEntity<List<Receiver>> getReceiverOfUser(@PathVariable UUID userId) {
        return userService.getReceiverOfUser(userId);
    }

//    @PostMapping("/{user_id}/add")
//    public ResponseEntity<String> addReceiverOfUser(@PathVariable UUID user_id, @RequestBody Receiver newReceiver) {
//        return userService.addReceiverOfUser(user_id, newReceiver);
//    }
}
