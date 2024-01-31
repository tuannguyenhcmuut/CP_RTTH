package org.ut.server.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.dtos.user.UserResponseDTO;
import org.ut.server.userservice.common.MessageConstants;
import org.ut.server.userservice.exception.AddressException;
import org.ut.server.userservice.exception.UserExistedException;
import org.ut.server.userservice.exception.UserNotFoundException;
import org.ut.server.userservice.mapper.UserMapper;
import org.ut.server.userservice.model.Address;
import org.ut.server.userservice.model.User;
import org.ut.server.userservice.repo.AddressRepository;
import org.ut.server.userservice.repo.ReceiverRepository;
import org.ut.server.userservice.repo.UserRepository;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ReceiverRepository receiverRepository;

    private final UserMapper userMapper;


    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK) ;
    }

    public UserResponseDTO getUserInfo(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
//            return new ResponseEntity<>("User Not found!", HttpStatus.BAD_REQUEST);

        return userMapper.mapEntityToResponse(user.get());
    }

    public List<Address> getAllAddress() {
        return addressRepository.findAll() ;
    }

    public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
        // catch exception if email or username is already exist
        Optional<User> emailEntry = userRepository.findUserByEmail(userRequestDTO.getEmail());
        Optional<User> usernameEntry = userRepository.findByUsername(userRequestDTO.getUsername());
        Optional<User> phoneNumberEntry = userRepository.findUserByPhoneNumber(userRequestDTO.getPhoneNumber());

        if (emailEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.EMAIL_EXISTED);
        }
        if (usernameEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.USERNAME_EXISTED);
        }
        if (phoneNumberEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.PHONE_EXISTED);
        }
        User newUser = userMapper.mapRequestToUser(userRequestDTO);
        newUser.setId(UUID.randomUUID());

        // mapping to User
        userRepository.save(newUser);

        return userMapper.mapEntityToResponse(newUser);
    }

    public UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        if (user.get().getId() != userId) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User id not match!");
        User userEntity = user.get();
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setUsername(userRequestDTO.getUsername());
        userEntity.setFirstName(userRequestDTO.getFirstName());
        userEntity.setLastName(userRequestDTO.getLastName());
        userEntity.setGender(userRequestDTO.getGender());
        userEntity.setPhoneNumber(userRequestDTO.getPhoneNumber());
        userEntity.setDateOfBirth(userRequestDTO.getDateOfBirth());
//
        // avatar
        // address

        userRepository.save(userEntity);
        return userMapper.mapEntityToResponse(userEntity);
    }

    public void deleteUserById(UUID id) {
        // find
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);

        userRepository.deleteById(id);
    }

    public Address addAddressForUserById(UUID id, Address address) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);

        List<Address> newList = user.get().getAddresses();
        newList.add(newList.size(), address);
        user.get().setAddresses(newList);

        address.setUser(user.get());
        Address newAddress = addressRepository.save(address);

        return newAddress;
    }

    public List<Address> getAddressForUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);

        return user.get().getAddresses();
    }



    public void deleteAddressById(UUID userId, Long id) {
//        find address
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()) throw new AddressException("Address not found!");

        if (address.get().getUser().getId() != userId) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User id not match!");
        addressRepository.deleteById(id);
    }

    public UserResponseDTO uploadAvatar(byte[] avatar, UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException("User Not found!");

        User userEntity = user.get();
        String avatarString = Base64.getEncoder().encodeToString(avatar);
        userEntity.setAvatar(avatarString);
        userRepository.save(userEntity);
        return userMapper.mapEntityToResponse(userEntity);
    }

    public UUID getUserIdByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new UserNotFoundException("User Not found!");

        return user.get().getId();
    }


//    public ResponseEntity<List<Receiver>> getReceiverOfUser(UUID user_id) {
//        Optional<User> user = userRepository.findById(user_id);
//        if(user.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//
//        return new ResponseEntity<>(user.get().getReceivers(), HttpStatus.OK);
//    }

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


    // get all address
    // add address
    // update address
    // remove address
}
