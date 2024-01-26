package org.ut.server.common.server.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private UUID userId;
    private String accessToken;
    private String message;
}
