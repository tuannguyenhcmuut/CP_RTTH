package org.ut.server.authservice.controller;

import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.ut.server.authservice.config.JwtUtils;
import org.ut.server.authservice.dto.AuthResponseDTO;
import org.ut.server.authservice.dto.LoginDto;
import org.ut.server.authservice.dto.RegisterDto;
import org.ut.server.authservice.model.Account;
import org.ut.server.authservice.model.CustomUserDetails;
import org.ut.server.authservice.service.AuthService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    // login
//    @RequestMapping("/login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String jwtToken = jwtUtils.generateToken(user);

        if (user != null) {
            return new ResponseEntity<>(
                    new AuthResponseDTO(
                            user.getUsername(),
                            jwtUtils.extractUserId(jwtToken),
                            jwtToken,
                            "Login successfully"
                    ),
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDTO) {
        return authService.register(registerDTO);
    }



    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return invalidTokenResponse();
        }

        authHeader = authHeader.substring(7);

        Map<String, Object> response = new HashMap<>();
        try {
            if (jwtUtils.isTokenValid(authHeader)) {
                return validTokenResponse(authHeader);
            }
        }
        catch (Exception e) {
            return invalidTokenResponse();
        }
        return invalidTokenResponse();
    }

    private ResponseEntity<Object>  invalidTokenResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "400");
        response.put("message", "Invalid token");
        response.put("data", null);
        response.put("timestamps", new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private  ResponseEntity<Object>  validTokenResponse(String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", "200");
        response.put("message", "Valid token");
        response.put("data", jwtUtils.extractUsername(token));
        response.put("timestamps", new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

// refresh token


// validate token

// logout
}