package org.ut.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ut.server.model.Address;
import org.ut.server.model.Receiver;
import org.ut.server.model.User;
import org.ut.server.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getUser() {
        return userService.getAllUser();
    }

    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAddress() {

        return userService.getAllAddress();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User newUser) {
        return userService.createNewUser(newUser);
    }

    //Get list of addresses for user base on user_id
    @GetMapping("/address/{id}")
    public ResponseEntity<List<Address>> getAddressForUserById(@PathVariable Long id) {
        return userService.getAddressForUserById(id);
    }

    //Add new address for user base on user_id
    @PostMapping("/address/{id}")
    public ResponseEntity<String> addAddressForUserById(@PathVariable Long id, @RequestBody Address address) {
        return userService.addAddressForUserById(id, address);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long id) {
        return userService.deleteAddressById(id);
    }

    //update user by id

    //get list_receiver by user_id
    @GetMapping("/{user_id}")
    public ResponseEntity<List<Receiver>> getReceiverOfUser(@PathVariable Long user_id) {
        return userService.getReceiverOfUser(user_id);
    }

//    @PostMapping("/{user_id}/add")
//    public ResponseEntity<String> addReceiverOfUser(@PathVariable Long user_id, @RequestBody Receiver newReceiver) {
//        return userService.addReceiverOfUser(user_id, newReceiver);
//    }
}
