package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.ut.server.omsserver.dto.ChatNotification;
import org.ut.server.omsserver.dto.MessageDto;
import org.ut.server.omsserver.mapper.MessageMapper;
import org.ut.server.omsserver.model.ChatMessage;
import org.ut.server.omsserver.service.ChatMessageService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final MessageMapper messageMapper;

    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public MessageDto register(@Payload MessageDto chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public MessageDto sendMessage(@Payload MessageDto chatMessage) {
        return chatMessage;
    }

    // mapped as /app/chat
    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDto chatMessage) {
        ChatMessage messageEntity = messageMapper.toEntity(chatMessage);
        ChatMessage savedMsg = chatMessageService.save(messageEntity);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getReceiverId()), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSender().getId(),
                        savedMsg.getReceiver().getId(),
                        savedMsg.getContent()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<MessageDto>> findChatMessages(@PathVariable UUID senderId,
                                                              @PathVariable UUID recipientId) {
        return ResponseEntity
                .ok(
                        messageMapper.toDtos(
                                chatMessageService.findChatMessages(senderId, recipientId)
                        )
                );
    }
}