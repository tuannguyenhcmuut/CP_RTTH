package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponseDto {
    private String message;
}
