package org.ut.server.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String username;
    private String userId;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
    private String message;
}
