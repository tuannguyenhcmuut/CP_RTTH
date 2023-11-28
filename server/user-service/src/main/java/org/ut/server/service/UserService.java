package org.ut.server.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ut.server.model.Address;
import org.ut.server.model.Receiver;
import org.ut.server.model.User;
import org.ut.server.repo.AddressRepository;
import org.ut.server.repo.ReceiverRepository;
import org.ut.server.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private AddressRepository addressRepository;

    private ReceiverRepository receiverRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, ReceiverRepository receiverRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.receiverRepository = receiverRepository;
    }

    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK) ;
    }

    public ResponseEntity<List<Address>> getAllAddress() {
        return new ResponseEntity<>(addressRepository.findAll(), HttpStatus.OK) ;
    }

    public ResponseEntity<String> createNewUser(User newUser) {
        userRepository.save(newUser);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addAddressForUserById(Long id, Address address) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<Address> newList = user.get().getAddresses();
        newList.add(newList.size(), address);
        user.get().setAddresses(newList);

        address.setUser(user.get());
        addressRepository.save(address);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<Address>> getAddressForUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user.get().getAddresses(), HttpStatus.OK);
    }

    public ResponseEntity<String> deleteUserById(Long id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteAddressById(Long id) {
        addressRepository.deleteById(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    public ResponseEntity<List<Receiver>> getReceiverOfUser(Long user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(user.get().getList_receiver(), HttpStatus.OK);
    }

//    public ResponseEntity<String> addReceiverOfUser(Long user_id, Receiver newReceiver) {
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
