package org.ut.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ut.server.dto.UserRequestDTO;
import org.ut.server.model.Address;
import org.ut.server.model.Receiver;
import org.ut.server.model.User;
import org.ut.server.repo.AddressRepository;
import org.ut.server.repo.ReceiverRepository;
import org.ut.server.repo.UserRepository;
import org.ut.server.util.UserMappingUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ReceiverRepository receiverRepository;

    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK) ;
    }

    public ResponseEntity<List<Address>> getAllAddress() {
        return new ResponseEntity<>(addressRepository.findAll(), HttpStatus.OK) ;
    }

    public ResponseEntity<String> createNewUser(UserRequestDTO userRequestDTO) {
        // catch exception if email or username is already exist
        Optional<User> emailEntry = userRepository.findUserByEmail(userRequestDTO.getEmail());
        Optional<User> usernameEntry = userRepository.findByUsername(userRequestDTO.getUsername());

        if (emailEntry.isPresent()) {
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        if (usernameEntry.isPresent()) {
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
        }
        User newUser = UserMappingUtil.mapUserRequestToUser(userRequestDTO);
        newUser.setId(UUID.randomUUID());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
//        newUserEntity.setUsername(newUser.getUsername());
        // mapping to User
        userRepository.save(newUser);
        return new ResponseEntity<>("Create user successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addAddressForUserById(UUID id, Address address) {
            Optional<User> user = userRepository.findById(id);
            if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            List<Address> newList = user.get().getAddresses();
        newList.add(newList.size(), address);
        user.get().setAddresses(newList);

        address.setUser(user.get());
        addressRepository.save(address);

        return new ResponseEntity<>("Add address to user address  ", HttpStatus.CREATED);
    }

    public ResponseEntity<List<Address>> getAddressForUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user.get().getAddresses(), HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUserById(UUID id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>("Delete user successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteAddressById(Long id) {
        addressRepository.deleteById(id);
        return new ResponseEntity<>("Delete address successfully", HttpStatus.OK);
    }

    public ResponseEntity<List<Receiver>> getReceiverOfUser(UUID user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user.get().getReceivers(), HttpStatus.OK);
    }

//    public ResponseEntity<String> addReceiverOfUser(UUID user_id, Receiver newReceiver) {
//        Optional<User> user = userRepository.findById(user_id);
//        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        List<Receiver> newListReceiver = user.get().getList_receiver();
//        newListReceiver.add(newListReceiver.size(), newReceiver);
//        user.get().setList_receiver(newListReceiver);
//
//        //TODO update receiver table
//
//        List<User> newListUser = newReceiver.getList_user();
//        newListUser.add(newListUser.size(), user.get());
//        newReceiver.setList_user(newListUser);
//        receiverRepository.save(newReceiver);
//
//        return new ResponseEntity<>("success", HttpStatus.CREATED);
//    }
}
