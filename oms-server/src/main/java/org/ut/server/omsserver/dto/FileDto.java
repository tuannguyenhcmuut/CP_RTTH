package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDto {
    private String base64;
    private Long sizeKB;
}
