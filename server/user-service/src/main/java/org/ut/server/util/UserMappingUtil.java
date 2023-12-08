package org.ut.server.util;

import org.ut.server.dto.UserRequestDTO;
import org.ut.server.dto.UserResponseDTO;
import org.ut.server.model.User;

public class UserMappingUtil {
    public static UserResponseDTO mapUserToUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .addresses(user.getAddresses())
                .gender(user.getGender())
                .receivers(user.getReceivers())
                .stores(user.getStores())
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
                .receivers(user.getReceivers())
                .stores(user.getStores())
                .build();
    }


}
