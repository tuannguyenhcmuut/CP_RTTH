package org.ut.server.userservice.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.dtos.user.UserResponseDTO;
import org.ut.server.userservice.exception.FileUploadException;
import org.ut.server.userservice.model.User;
import utils.FileUtils;

import java.sql.SQLException;

@Component
public class UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    public UserResponseDTO mapEntityToResponse(User user) {
        if (user == null) {
            return null;
        }
        try {
            return UserResponseDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .gender(user.getGender())
                    .avatar(FileUtils.blobToBase64(user.getAvatar()))
                    .dateOfBirth(user.getDateOfBirth())
                    .addresses(addressMapper.mapEntitiesToDtos(user.getAddresses()))  // todo: fix addresses
                    .phoneNumber(user.getPhoneNumber())
                    .build();
        } catch (SQLException e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    public User mapRequestToUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }
        return User.builder()
                .email(userRequestDTO.getEmail())
                .username(userRequestDTO.getUsername())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .addresses(addressMapper.mapDtosToEntities(userRequestDTO.getAddresses()))
                .gender(userRequestDTO.getGender())
                .build();
    }
}
