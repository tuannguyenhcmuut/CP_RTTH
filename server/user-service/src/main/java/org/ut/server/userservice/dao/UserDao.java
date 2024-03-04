package org.ut.server.userservice.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.model.Account;
import org.ut.server.userservice.repo.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDao {
    private final AccountRepository accountRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> userAccount = accountRepository.findAccountByUsername(username);
        if (userAccount.isPresent() == false) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // build GrantedAuthority for roles

        List<String> rolesList = userAccount.get().getRoles().stream()
                .map(role -> role.toString())
                .collect(Collectors.toList());

        String[] rolesArray = rolesList.toArray(new String[0]);

        return User
                .withUsername(userAccount.get().getUsername())
                .password(userAccount.get().getPassword())
                .roles(rolesArray) // Set roles if you have a role-based system
                .build();
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
