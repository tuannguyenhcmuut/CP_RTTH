package org.ut.server.authservice.server.common.server.util;

import org.ut.server.authservice.server.common.server.model.User;
import org.ut.server.authservice.server.common.server.dto.UserRequestDTO;
import org.ut.server.authservice.server.common.server.dto.UserResponseDTO;

public class UserMappingUtil {
    public static UserResponseDTO mapUserToUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .addresses(user.getAddresses())
                .gender(user.getGender())
        .build();
    }

    public static User mapUserRequestToUser(UserRequestDTO user) {
        return User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .addresses(user.getAddresses())
                .gender(user.getGender())
                .build();
    }


}