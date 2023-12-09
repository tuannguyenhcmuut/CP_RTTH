package org.ut.server.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ut.server.dto.UserRequestDTO;

@Service
@AllArgsConstructor
public class AuthService {
//    private final RestTemplate restTemplate;
//    private final JwtUtils jwtUtils;
//    private final UserService userService;

    public ResponseEntity<String> register(UserRequestDTO authRequest) {
//        String message = userService.createNewUser(authRequest);
//        if (message.equals("Email is already exist!")) {
//            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);;
//        }
//        else if (message.equals("User already exists")) {
//            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
//        }
//
//
//
//        return new ResponseEntity<>("Register successfully", HttpStatus.CREATED);
        return null;
    }
}
