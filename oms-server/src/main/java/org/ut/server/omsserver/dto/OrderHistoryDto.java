package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderHistoryDto {
    private LocalDateTime actionDate;
    private String description;
}
