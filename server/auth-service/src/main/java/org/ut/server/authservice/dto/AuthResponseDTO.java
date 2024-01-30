package org.ut.server.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String username;
    private String userId;
    private String accessToken;
    private String message;
}
