package org.ut.server.common.server.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.common.server.model.User;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.dtos.user.UserResponseDTO;

@Component
public class UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    public UserResponseDTO mapEntityToResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .avatar(user.getAvatar())
                .dateOfBirth(user.getDateOfBirth())
                .addresses(addressMapper.mapEntitiesToDtos(user.getAddresses()))  // todo: fix addresses
                .phoneNumber(user.getPhoneNumber())
                .build();
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
