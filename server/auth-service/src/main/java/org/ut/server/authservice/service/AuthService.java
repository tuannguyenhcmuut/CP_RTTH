package org.ut.server.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ut.server.authservice.config.UserFeign;
import org.ut.server.authservice.dto.RegisterDto;
import org.ut.server.authservice.model.Account;
import org.ut.server.authservice.repository.AccountRepository;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.utils.JsonMapperUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserFeign userFeign;

    @Transactional
    public ResponseEntity<?> register(RegisterDto registerDTO) throws JsonProcessingException {
            Optional<Account> account = accountRepository.findById(registerDTO.getUsername());
        if(account.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }



        // create new User in User-Service with username, email, phone, firstname, lastname
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .phoneNumber(registerDTO.getPhoneNumber())
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .build();



        try {
            userFeign.createUser(userRequestDTO);
        }
        catch (FeignException e) {
            log.error(e.getMessage());
            String responseBody = e.contentUTF8();

            return ResponseEntity.badRequest().body(
                    JsonMapperUtils.toErrorResponseJson(responseBody).getMessage()
            );
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("Something went wrong");
        }

        Account newAccount = Account.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .build();
        accountRepository.save(newAccount);

          return ResponseEntity.ok(MessageFormat.format("User {0} registered successfully!", userRequestDTO.getUsername()));
    }
}
