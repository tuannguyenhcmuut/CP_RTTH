package org.ut.server.userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.ut.server.userservice.model.*;
import org.ut.server.userservice.model.enums.ERole;
import org.ut.server.userservice.repo.AccountRepository;
import org.ut.server.userservice.repo.ShipperRepository;
import org.ut.server.userservice.repo.ShopOwnerRepository;
import org.ut.server.userservice.repo.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

//    @Autowired
    private final AccountRepository accountRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final ShipperRepository shipperRepository;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/auth/login",
                        "/auth/register",
                        "/auth/validate",
                        "/auth/refreshToken"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(this.authenticationProvider())
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
//        http.formLogin()
//            .and().httpBasic();
        return http.build();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(exceptionResolver);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
               Account user = accountRepository.findAccountByUsername(username).orElseThrow(
                          () -> new UsernameNotFoundException("Username not found: " + username)
               );

                Set<Role> roles = user.getRoles();
                if (checkRoleSet(user.getRoles(),ERole.ROLE_USER)) {
//                    log.debug("Shop owner found: " + user.getUser().getId());

                    // find user service
                    try {
                        ShopOwner shopOwner = shopOwnerRepository.findByAccount_Username(username).orElseThrow(
                                () -> new UsernameNotFoundException("Username not found" + username)
                        );
                        UUID userId = shopOwner.getId();

                        log.debug("UserId found: " + userId);
                        return CustomUserDetails.build(user, userId);
                    } catch (Exception e) {
                        throw new UsernameNotFoundException("Username not found" + username);
                    }
                }
                else if (user.getRoles().contains(ERole.ROLE_SHIPPER)){
                    // shipper
                    try {
                        Shipper shipper = shipperRepository.findByAccount_Username(username).orElseThrow(
                                () -> new UsernameNotFoundException("Username not found" + username)
                        );
                        UUID userId = shipper.getId();
                        log.debug("UserId found: " + userId);
                        return CustomUserDetails.build(user, userId);
                    } catch (Exception e) {
                        throw new UsernameNotFoundException("Username not found" + username);
                    }
                }
                else {
                    throw new UsernameNotFoundException("Username not found" + username);
                }

            }

        };
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

}
