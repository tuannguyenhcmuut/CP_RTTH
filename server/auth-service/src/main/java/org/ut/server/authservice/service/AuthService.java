package org.ut.server.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ut.server.authservice.config.UserFeign;
import org.ut.server.authservice.dto.RegisterDto;
import org.ut.server.authservice.model.Account;
import org.ut.server.authservice.repository.AccountRepository;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.exception.MessageCode;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserFeign userFeign;
    public ResponseEntity<?> register(RegisterDto registerDTO) {
            Optional<Account> account = accountRepository.findById(registerDTO.getUsername());
        if(account.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        Account newAccount = Account.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .build();
        accountRepository.save(newAccount);

        // create new User in User-Service with username, email, phone, firstname, lastname
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .phoneNumber(registerDTO.getPhoneNumber())
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .build();

        if (!userFeign.createUser(userRequestDTO).getCode().equals(MessageCode.CREATED_SUCCESS.toString())) {
            return ResponseEntity.badRequest().body("Username or email is already taken");
        }

          return ResponseEntity.ok(MessageFormat.format("User {0} registered successfully!", userRequestDTO.getUsername()));
    }
}
