package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.FileDto;
import org.ut.server.omsserver.dto.request.UserRequestDTO;
import org.ut.server.omsserver.dto.response.UserResponseDTO;
import org.ut.server.omsserver.exception.*;
import org.ut.server.omsserver.mapper.AddressMapper;
import org.ut.server.omsserver.mapper.ShopOwnerMapper;
import org.ut.server.omsserver.model.Address;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.repo.AddressRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopOwnerService {
    private final ShopOwnerRepository shopOwnerRepository;
    private final ShopOwnerMapper shopOwnerMapper;

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final EntityManagerFactory emf;


    private final IImageService imageService;


    public ResponseEntity<List<ShopOwner>> getAllUser() {
        return new ResponseEntity<>(shopOwnerRepository.findAll(), HttpStatus.OK);
    }

    public UserResponseDTO getUserInfo(UUID userId) {
        Optional<ShopOwner> user = shopOwnerRepository.findById(userId);
        if (user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
//            return new ResponseEntity<>(MessageConstants.SHOP_OWNER_NOT_FOUND, HttpStatus.BAD_REQUEST);

        return shopOwnerMapper.mapEntityToResponse(user.get());
    }

    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) throws SQLException {
        // catch exception if email or username is already exist
        Optional<ShopOwner> emailEntry = shopOwnerRepository.findUserByEmail(userRequestDTO.getEmail());
        Optional<ShopOwner> usernameEntry = shopOwnerRepository.findByAccount_Username(userRequestDTO.getUsername());
        Optional<ShopOwner> phoneNumberEntry = shopOwnerRepository.findUserByPhoneNumber(userRequestDTO.getPhoneNumber());

        if (emailEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.EMAIL_EXISTED);
        }
        if (usernameEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.USERNAME_EXISTED);
        }
        if (phoneNumberEntry.isPresent()) {
            throw new UserExistedException(MessageConstants.PHONE_EXISTED);
        }
        ShopOwner newUser = shopOwnerMapper.mapRequestToUser(userRequestDTO);
        newUser.setId(UUID.randomUUID());

        // mapping to ShopOwner
        shopOwnerRepository.save(newUser);

        return shopOwnerMapper.mapEntityToResponse(newUser);
    }

    @Transactional
    public UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        Optional<ShopOwner> user = shopOwnerRepository.findById(userId);
        if (user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        if (user.get().getId() != userId)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, MessageConstants.SHOP_OWNER_ID_NOT_MATCH);
        ShopOwner userEntity = user.get();
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

        if (userRequestDTO.getAvatar() != null) {
            userEntity.setAvatarUrl(userRequestDTO.getAvatar());
        }

        if (userRequestDTO.getAddresses() != null) {
            userEntity.getAddresses().clear();
            userEntity.setAddresses(
                    addressMapper.mapDtosToEntities(userRequestDTO.getAddresses())
            );
        }
        userEntity.setLastLogin(LocalDateTime.now());
        try {
            userEntity = shopOwnerRepository.save(userEntity);
            shopOwnerRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new UserException(MessageConstants.PHONE_EXISTED);
        }
        catch (Exception e) {
            throw new UserException(MessageConstants.ERROR_UPDATE_USER_INFO);
        }

        return shopOwnerMapper.mapEntityToResponse(userEntity);
    }

    public void deleteUserById(UUID id) {
        // find
        Optional<ShopOwner> user = shopOwnerRepository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);

        shopOwnerRepository.deleteById(id);
    }

    public Address addAddressForUserById(UUID id, Address address) {
        Optional<ShopOwner> user = shopOwnerRepository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);

        List<Address> newList = user.get().getAddresses();
        newList.add(newList.size(), address);
        user.get().setAddresses(newList);

        address.setShopOwner(user.get());

        return addressRepository.save(address);
    }

    public List<Address> getAddressForUserById(UUID id) {
        Optional<ShopOwner> user = shopOwnerRepository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);

        return user.get().getAddresses();
    }


    public void deleteAddressById(UUID userId, Long id) {
        Optional<Address> address = addressRepository.findById(id);
        if (address.isEmpty()) throw new AddressException("Address not found!");

        if (address.get().getShopOwner().getId() != userId)
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, MessageConstants.SHOP_OWNER_ID_NOT_MATCH);
        addressRepository.deleteById(id);
    }

    public UserResponseDTO uploadAvatar(MultipartFile avatar, UUID userId) throws IOException {
        ShopOwner user = shopOwnerRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(MessageConstants.SHOP_OWNER_NOT_FOUND)
        );
        String filename = imageService.save(avatar);
        String avatarString = imageService.getImageUrl(filename);
        user.setAvatarUrl(avatarString);
        shopOwnerRepository.save(user);
        return shopOwnerMapper.mapEntityToResponse(user);
    }

    public UUID getUserIdByUsername(String username) {
        Optional<ShopOwner> user = shopOwnerRepository.findByAccount_Username(username);
        if (user.isEmpty()) throw new UserNotFoundException(MessageConstants.SHOP_OWNER_NOT_FOUND);

        return user.get().getId();
    }

    public FileDto uploadImage(MultipartFile imageFile) throws IOException {
        String fileName = imageService.save(imageFile);
        String imageUrl = imageService.getImageUrl(fileName);
        return FileDto.builder()
                .base64(imageUrl)
                .sizeKB(null) // get file size in kb
                .build();
    }


    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}