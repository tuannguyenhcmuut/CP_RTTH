package org.ut.server.common.server.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.ut.server.common.server.common.MessageConstants;
import org.ut.server.common.server.dto.UserRequestDTO;
import org.ut.server.common.server.dto.UserResponseDTO;
import org.ut.server.common.server.enums.Gender;
import org.ut.server.common.server.exception.UserExistedException;
import org.ut.server.common.server.exception.UserNotFoundException;
import org.ut.server.common.server.mapper.UserMapper;
import org.ut.server.common.server.util.UserMappingUtil;
import org.ut.server.common.server.model.Address;
import org.ut.server.common.server.model.Receiver;
import org.ut.server.common.server.model.User;
import org.ut.server.common.server.repo.AddressRepository;
import org.ut.server.common.server.repo.ReceiverRepository;
import org.ut.server.common.server.repo.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private final PasswordEncoder passwordEncoder;
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

    public ResponseEntity<List<Address>> getAllAddress() {
        return new ResponseEntity<>(addressRepository.findAll(), HttpStatus.OK) ;
    }

    public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
        // catch exception if email or username is already exist
        Optional<User> emailEntry = userRepository.findUserByEmail(userRequestDTO.getEmail());
        Optional<User> usernameEntry = userRepository.findByUsername(userRequestDTO.getUsername());

        if (emailEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.EMAIL_EXISTED);
        }
        if (usernameEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.USERNAME_EXISTED);
        }
        User newUser = userMapper.mapRequestToUser(userRequestDTO);
        newUser.setId(UUID.randomUUID());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRegisterAt(LocalDateTime.now());
//        newUserEntity.setUsername(newUser.getUsername());
        // mapping to User
        userRepository.save(newUser);

        return userMapper.mapEntityToResponse(newUser);
    }

    public UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        if (user.get().getId() != userId) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User id not match!");
        User userEntity = user.get();
//        UserMappingUtil.mapUserRequestToUser(userRequestDTO, userEntity);
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setUsername(userRequestDTO.getUsername());
        // dont update password
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



    public ResponseEntity<String> deleteAddressById(Long id) {
        addressRepository.deleteById(id);
        return new ResponseEntity<>("Delete address successfully", HttpStatus.OK);
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
