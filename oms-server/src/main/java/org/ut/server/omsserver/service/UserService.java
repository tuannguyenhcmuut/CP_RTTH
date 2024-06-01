package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.FileDto;
import org.ut.server.omsserver.dto.request.UserRequestDTO;
import org.ut.server.omsserver.dto.response.UserResponseDTO;
import org.ut.server.omsserver.exception.AddressException;
import org.ut.server.omsserver.exception.UserException;
import org.ut.server.omsserver.exception.UserNotFoundException;
import org.ut.server.omsserver.mapper.AddressMapper;
import org.ut.server.omsserver.mapper.UserMapper;
import org.ut.server.omsserver.model.Address;
import org.ut.server.omsserver.model.User;
import org.ut.server.omsserver.repo.AddressRepository;
import org.ut.server.omsserver.repo.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;
    private final EntityManagerFactory emf;
    private final IImageService imageService;


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

//    public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
//        // catch exception if email or username is already exist
//        Optional<User> emailEntry = userRepository.findUserByEmail(userRequestDTO.getEmail());
//        Optional<User> usernameEntry = userRepository.findByAccount_Username(userRequestDTO.getUsername());
//        Optional<User> phoneNumberEntry = userRepository.findUserByPhoneNumber(userRequestDTO.getPhoneNumber());
//
//        if (emailEntry.isPresent()) {
//            throw new UserExistedException(MessageConstants.EMAIL_EXISTED);
//        }
//        if (usernameEntry.isPresent()) {
//            throw new UserExistedException(MessageConstants.USERNAME_EXISTED);
//        }
//        if (phoneNumberEntry.isPresent()) {
//            throw new UserExistedException(MessageConstants.PHONE_EXISTED);
//        }
//        User newUser = userMapper.mapRequestToUser(userRequestDTO);
//        newUser.setId(UUID.randomUUID());
//
//        // mapping to User
//        userRepository.save(newUser);
//
//        return userMapper.mapEntityToResponse(newUser);
//    }

    @Transactional
    public UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        if (user.get().getId() != userId) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User id not match!");
        User userEntity = user.get();
        userEntity.setFirstName(
                userRequestDTO.getFirstName() != null ? userRequestDTO.getFirstName() : userEntity.getFirstName()
        );
        userEntity.setLastName(
                userRequestDTO.getLastName() != null ? userRequestDTO.getLastName() : userEntity.getLastName()
        );
        userEntity.setGender(
                userRequestDTO.getGender() != null ? userRequestDTO.getGender() : userEntity.getGender()
        );
        try {
            userEntity.setPhoneNumber(
                    userRequestDTO.getPhoneNumber() != null ? userRequestDTO.getPhoneNumber() : userEntity.getPhoneNumber()
            );
        } catch (Exception e) {
            throw new UserException(MessageConstants.PHONE_EXISTED);
        }

        userEntity.setDateOfBirth(
                userRequestDTO.getDateOfBirth() != null ? userRequestDTO.getDateOfBirth() : userEntity.getDateOfBirth()
        );
        userEntity.setAvatarUrl(
                userEntity.getAvatarUrl()
        );


        if (userRequestDTO.getAddresses() != null ) {
//            userEntity.getAddresses().clear();
//            userEntity.setAddresses(
//                    addressMapper.mapDtosToEntities(userRequestDTO.getAddresses())
//                );
        }
        userEntity.setLastLogin(LocalDateTime.now());

        userEntity = userRepository.save(userEntity);
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

//        List<Address> newList = user.get().getAddresses();
//        newList.add(newList.size(), address);
//        user.get().setAddresses(newList);
//
//        address.setUser(user.get());
        Address newAddress = addressRepository.save(address);

        return newAddress;
    }

//    public List<Address> getAddressForUserById(UUID id) {
//        Optional<User> user = userRepository.findById(id);
//        if(user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
//
//        return user.get().getAddresses();
//    }



    public void deleteAddressById(UUID userId, Long id) {
//        find address
        Optional<Address> address = addressRepository.findById(id);
        if(address.isEmpty()) throw new AddressException("Address not found!");

//        if (address.get().getUser().getId() != userId) throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User id not match!");
        addressRepository.deleteById(id);
    }

    public UserResponseDTO uploadAvatar(MultipartFile avatar, UUID userId) throws IOException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException("User Not found!");

        User userEntity = user.get();
        String avatarString = uploadImage(avatar).getBase64();

//        try {
////            userEntity.setAvatarUrl(FileUtils.base64ToBlob(avatarString));
//        } catch (SQLException e) {
//            throw new FileUploadException("Error converting photo to Blob. " +e.getMessage());
//        }
        userRepository.save(userEntity);
        return userMapper.mapEntityToResponse(userEntity);
    }

//    public UUID getUserIdByUsername(String username) {
//        Optional<User> user = userRepository.findByAccount_Username(username);
//        if(user.isEmpty()) throw new UserNotFoundException("User Not found!");
//
//        return user.get().getId();
//    }

    public FileDto uploadImage(MultipartFile file) throws IOException {
        String filename = imageService.save(file);
        String fileUrl = imageService.getImageUrl(filename);

        return FileDto.builder()
                .base64(fileUrl)
                .sizeKB(null) // get file size in kb
                .build();
    }


    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
