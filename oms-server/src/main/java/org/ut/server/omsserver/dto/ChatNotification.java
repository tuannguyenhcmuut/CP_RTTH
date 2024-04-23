package org.ut.server.omsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ChatNotification {
    //    public ChatNotification(Long id, Object senderId, Object recipientId, String content) {
//    }
    private Long id;
    private UUID senderId;
    private UUID recipientId;
    private String content;
}
