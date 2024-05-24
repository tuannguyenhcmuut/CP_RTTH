package org.ut.server.omsserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime actionDate;
}
