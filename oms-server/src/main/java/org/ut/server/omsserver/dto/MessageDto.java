package org.ut.server.omsserver.dto;

import lombok.Builder;
import lombok.Data;
import org.ut.server.omsserver.model.enums.MessageType;

import java.util.UUID;

@Data
@Builder
public class MessageDto {
    private Long id;
    private String chatId;
    private UUID senderId;
    private UUID receiverId;
    private String content;
    private MessageType type;
}