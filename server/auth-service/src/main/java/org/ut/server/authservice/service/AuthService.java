package org.ut.server.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ut.server.authservice.config.UserFeign;
import org.ut.server.authservice.dto.request.RegisterDto;
import org.ut.server.authservice.model.Account;
import org.ut.server.authservice.model.ERole;
import org.ut.server.authservice.model.Role;
import org.ut.server.authservice.repository.AccountRepository;
import org.ut.server.authservice.repository.RoleRepository;
import org.ut.server.common.dtos.user.UserRequestDTO;
import org.ut.server.common.utils.JsonMapperUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserFeign userFeign;
    private final RoleRepository roleRepository;

    @Transactional
    public ResponseEntity<?> register(RegisterDto registerDTO) throws JsonProcessingException {
            Optional<Account> account = accountRepository.findById(registerDTO.getUsername());
        if(account.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        Set<String> strRoles = registerDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_SHOPOWNER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "shipper":
                        Role modRole = roleRepository.findByName(ERole.ROLE_SHIPPER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    case "employee":
                        Role employeeRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(employeeRole);

                        break;

                    case "showOwner":
                        Role showOwnerRole = roleRepository.findByName(ERole.ROLE_SHOPOWNER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(showOwnerRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_SHOPOWNER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
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
                .roles(roles)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .build();
        accountRepository.save(newAccount);

          return ResponseEntity.ok(MessageFormat.format("User {0} registered successfully!", userRequestDTO.getUsername()));
    }
}
