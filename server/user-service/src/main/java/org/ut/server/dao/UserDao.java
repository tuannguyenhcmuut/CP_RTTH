package org.ut.server.dao;

import com.netflix.discovery.converters.Auto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.ut.server.model.CustomUserDetails;
import org.ut.server.model.User;
import org.ut.server.repo.UserRepository;

import java.util.Collections;
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

    public UserDetails loadUserByUserId(String userId) {
        User user = userRepository.findById(UUID.fromString(userId)).get();
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }


        return new CustomUserDetails(user);
    }


}
