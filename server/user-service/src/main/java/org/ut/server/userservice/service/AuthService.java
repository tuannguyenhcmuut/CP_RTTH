package org.ut.server.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.mapper.ShipperMapper;
import org.ut.server.userservice.mapper.ShopOwnerMapper;
import org.ut.server.userservice.model.*;
import org.ut.server.userservice.model.enums.ERole;
import org.ut.server.userservice.repo.AccountRepository;
import org.ut.server.userservice.repo.RoleRepository;
import org.ut.server.userservice.repo.ShipperRepository;
import org.ut.server.userservice.repo.ShopOwnerRepository;

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
//    private final UserFeign userFeign;
    private final RoleRepository roleRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final ShipperRepository shipperRepository;
    private final ShopOwnerMapper shopOwnerMapper;
    private final ShipperMapper shipperMapper;

    @Transactional
    public ResponseEntity<?> register(org.ut.server.userservice.dto.request.RegisterDto registerDTO) throws JsonProcessingException {
            Optional<Account> account = accountRepository.findById(registerDTO.getUsername());
        if(account.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        Set<String> strRoles = registerDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
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

                    case "user":
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);

                        break;
                    default:
                        throw new RuntimeException("Error: Role is not found.");
                }
            });
        }

        Account newAccount = Account.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .roles(roles)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .build();

//        // create new User in User-Service with username, email, phone, firstname, lastname
//        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
//                .username(registerDTO.getUsername())
//                .email(registerDTO.getEmail())
//                .phoneNumber(registerDTO.getPhoneNumber())
//                .firstName(registerDTO.getFirstName())
//                .lastName(registerDTO.getLastName())
//                .build();

        assert strRoles != null;
        if (strRoles.contains("shipper")) {
            Shipper shipper = shipperMapper.newShipper(newAccount, registerDTO);
            Shipper savedUser = shipperRepository.save(shipper);
            return ResponseEntity.ok(MessageFormat.format("Shipper {0} registered successfully!", savedUser.getAccount().getUsername()));
        }
        if (strRoles.contains("user")) {
            ShopOwner shopOwner = shopOwnerMapper.newShopOwner(newAccount, registerDTO);
            ShopOwner savedUser = shopOwnerRepository.save(shopOwner);
            return ResponseEntity.ok(MessageFormat.format("User {0} registered successfully!", savedUser.getAccount().getUsername()));
        }
        else {
            ShopOwner shopOwner = shopOwnerMapper.newShopOwner(newAccount, registerDTO);
            ShopOwner savedUser = shopOwnerRepository.save(shopOwner);
            return ResponseEntity.ok(MessageFormat.format("User {0} registered successfully!", savedUser.getAccount().getUsername()));
        }
    }
}
