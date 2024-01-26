package org.ut.server.authservice.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ut.server.authservice.model.Account;
import org.ut.server.authservice.repository.AccountRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDao {
    private final AccountRepository accountRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> user = accountRepository.findAccountByUsername(username);
        if (user.isPresent() == false) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return User
                .withUsername(user.get().getUsername())
                .password(user.get().getPassword())
                .roles("USER") // Set roles if you have a role-based system
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
