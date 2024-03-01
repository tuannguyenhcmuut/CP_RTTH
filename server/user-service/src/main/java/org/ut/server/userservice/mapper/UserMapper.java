package org.ut.server.userservice.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ut.server.userservice.dto.request.UserRequestDTO;
import org.ut.server.userservice.dto.response.UserResponseDTO;
import org.ut.server.userservice.exception.FileUploadException;
import org.ut.server.userservice.model.Account;
import org.ut.server.userservice.model.User;
import org.ut.server.userservice.repo.AccountRepository;
import org.ut.server.userservice.utils.FileUtils;

import java.sql.SQLException;

@Component
public class UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AccountRepository accountRepository;

    public UserResponseDTO mapEntityToResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
//                    .username(user.getAccount().getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender())
                .avatar(user.getAvatarUrl())
                .dateOfBirth(user.getDateOfBirth())
//                    .addresses(addressMapper.mapEntitiesToDtos(user.getAddresses()))  // todo: fix addresses
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User mapRequestToUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return null;
        }

        Account account = accountRepository.findAccountByUsername(userRequestDTO.getUsername()).get();
        User user = new User();
        user.setEmail(userRequestDTO.getEmail());
//        user.setAccount(account);
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setGender(userRequestDTO.getGender());
        user.setDateOfBirth(userRequestDTO.getDateOfBirth());

        return user;
    }
}
