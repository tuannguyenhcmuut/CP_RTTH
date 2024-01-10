package org.ut.server.common.server.mapper;

import org.springframework.stereotype.Component;
import org.ut.server.common.server.dto.UserRequestDTO;
import org.ut.server.common.server.dto.UserResponseDTO;
import org.ut.server.common.server.model.User;
import org.ut.server.common.server.repo.UserRepository;

@Component
public class UserMapper {

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
                .addresses(user.getAddresses())  // todo: fix addresses
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
                .password(userRequestDTO.getPassword())
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .addresses(userRequestDTO.getAddresses())
                .gender(userRequestDTO.getGender())
                .build();
    }
}
