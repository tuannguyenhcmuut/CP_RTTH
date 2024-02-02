package org.ut.server.authservice.controller;

import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.ut.server.authservice.config.JwtUtils;
import org.ut.server.authservice.dto.request.TokenRefreshRequest;
import org.ut.server.authservice.dto.response.AuthResponseDTO;
import org.ut.server.authservice.dto.request.LoginDto;
import org.ut.server.authservice.dto.request.RegisterDto;
import org.ut.server.authservice.dto.response.TokenRefreshResponseDto;
import org.ut.server.authservice.model.CustomUserDetails;
import org.ut.server.authservice.model.RefreshToken;
import org.ut.server.authservice.service.AuthService;
import org.ut.server.authservice.service.RefreshTokenService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    @Autowired
    private final RefreshTokenService refreshTokenService;

    // login
//    @RequestMapping("/login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDTO) {
        Authentication authentication;
//        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
//        }
//        catch (Exception ex) {
//            throw new BadCredentialsException("Invalid username/password");
//        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String jwtToken = jwtUtils.generateToken(userDetails);

        if (userDetails != null) {
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

            return new ResponseEntity<>(
                    new AuthResponseDTO(
                            userDetails.getUsername(),
                            jwtUtils.extractUserId(jwtToken),
                            jwtToken,
                            refreshToken.getToken(),
                            roles,
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
        try {
            return authService.register(registerDTO);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // refresh token
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponseDto response = refreshTokenService.refreshToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // logout



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