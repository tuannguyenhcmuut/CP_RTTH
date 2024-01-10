package org.ut.server.common.server.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class ReceiverDto {
    private Long id;
    private String username;
    private String phoneNumber;
    private String address;
    private UUID userId;
    private Boolean receivedAtPost;
    private String postAddress;
    private String note;
}
