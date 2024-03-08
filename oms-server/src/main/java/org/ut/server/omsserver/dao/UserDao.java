package org.ut.server.omsserver.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.model.*;
import org.ut.server.omsserver.model.enums.ERole;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.model.enums.PermissionLevel;
import org.ut.server.omsserver.repo.AccountRepository;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.ShipperRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDao {
    private final AccountRepository accountRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final ShipperRepository shipperRepository;
    private final EmployeeManagementRepository employeeManagementRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account userAccount = accountRepository.findAccountByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with username: " + username)
                );

        // build GrantedAuthority for roles

        List<String> rolesList = userAccount.getRoles().stream()
                .map(role -> role.toString())
                .collect(Collectors.toList());

        String[] rolesArray = rolesList.toArray(new String[0]);

        Set<Role> roles = userAccount.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();

        UUID userId;

        if (checkRoleSet(userAccount.getRoles(), ERole.ROLE_USER)) {
            // log.debug("Shop owner found: " + user.getUser().getId());
            // find user service
            try {
                ShopOwner shopOwner = shopOwnerRepository.findByAccount_Username(username).orElseThrow(
                        () -> new UsernameNotFoundException("Username not found" + username));
                userId = shopOwner.getId();

                log.debug("UserId found: " + userId);
                // add roles to authorities
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName().name())));
                // check if user is employee
                if (userAccount.getRoles().stream().anyMatch(role -> role.getName().name().equals("ROLE_EMPLOYEE"))) {
                    // add permissions to authorities
                    List<EmployeeManagement> empl_mangments = employeeManagementRepository
                            .findEmployeeManagementsByEmployeeId_IdAndApprovalStatus(userId, EmployeeRequestStatus.ACCEPTED).orElse(null);
                    empl_mangments.stream().map(
                            empl_mangment -> {
                                authorities.add(
                                        new SimpleGrantedAuthority(
                                                empl_mangment.getPermissionLevel().toString()));
                                return empl_mangment;
                            });

                    for (EmployeeManagement empl_mangment : empl_mangments) {
                        if (!empl_mangment.getPermissionLevel().isEmpty()) {
                            for (PermissionLevel permission : empl_mangment.getPermissionLevel()) {
//                                check if the permission is already in the authorities by convert GrantedAuthority to string
                                if (!authorities.contains(new SimpleGrantedAuthority(permission.toString())))
                                    authorities.add(
                                            new SimpleGrantedAuthority(permission.toString()));
                            }
                        }
                    }
                }
//                return CustomUserDetails.build(userAccount, userId, authorities);
            } catch (Exception e) {
                throw new UsernameNotFoundException("Username not found" + username);
            }
        } else if (userAccount.getRoles().contains(ERole.ROLE_SHIPPER)) {
            // shipper
            try {
                Shipper shipper = shipperRepository.findByAccount_Username(username).orElseThrow(
                        () -> new UsernameNotFoundException("Username not found" + username));
                userId = shipper.getId();
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName().name())));
                log.debug("UserId found: " + userId);
//                return CustomUserDetails.build(userAccount, userId, authorities);
            } catch (Exception e) {
                throw new UsernameNotFoundException("Username not found" + username);
            }
        } else {
            throw new UsernameNotFoundException("Username not found" + username);
        }

        return User
                .withUsername(userAccount.getUsername())
                .password(userAccount.getPassword())
                .roles(rolesArray) // Set roles if you have a role-based system
                .authorities(authorities)
                .build();
    }

    private boolean checkRoleSet(Set<Role> roles, ERole eRole) {
        // loop through roles and check if it contains the erole
        for (Role role : roles) {
            if (role.getName().equals(eRole)) {
                return true;
            }
        }
        return false;
    }
//    public boolean loadUserByUserId(String userId) {
//        User user = accountRepository.findById(UUID.fromString(userId)).get();
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with id: " + userId);
//        }
//
//
//        return new CustomUserDetails(user);
//    }
}
