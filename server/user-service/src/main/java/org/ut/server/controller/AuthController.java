package org.ut.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.ut.server.config.JwtUtils;
import org.ut.server.dao.UserDao;
import org.ut.server.dto.LoginDTO;
import org.ut.server.dto.AuthResponseDTO;
import org.ut.server.model.CustomUserDetails;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String jwtToken = jwtUtils.generateToken(user);

        if(user != null) {
            return new ResponseEntity<>(
                        new AuthResponseDTO (
                                user.getUserId(),
                                jwtToken,
                                "Login successfully"
                        ),
                        HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        String userId;
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }

        authHeader = authHeader.substring(7);
        userId = jwtUtils.extractUserID(authHeader);

        // && SecurityContextHolder.getContext().getAuthentication() == null
        if(userId != null ) {
            CustomUserDetails userDetails = (CustomUserDetails) userDao.loadUserByUserId(userId);
                if(jwtUtils.isTokenValid(authHeader, userDetails)) {
                    return new ResponseEntity<>("Valid token", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
    }
}
