package org.ut.server.authservice.server.common.server.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.ut.server.authservice.server.common.server.model.CustomUserDetails;
import org.ut.server.authservice.server.common.server.model.User;
import org.ut.server.authservice.server.common.server.repo.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDao implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetails  loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepository.findByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // Set roles if you have a role-based system
                .build();
    }

    public CustomUserDetails loadUserByUserId(String userId) {
        User user = userRepository.findById(UUID.fromString(userId)).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }


        return new CustomUserDetails(user);
    }


}
