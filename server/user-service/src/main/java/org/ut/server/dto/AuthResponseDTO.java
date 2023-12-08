package org.ut.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private UUID userId;
    private String token;
    private String message;
}
