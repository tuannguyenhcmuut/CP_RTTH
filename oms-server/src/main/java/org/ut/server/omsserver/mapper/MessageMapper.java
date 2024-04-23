package org.ut.server.omsserver.mapper;


import org.springframework.stereotype.Component;
import org.ut.server.omsserver.dto.MessageDto;
import org.ut.server.omsserver.model.ChatMessage;
import org.ut.server.omsserver.model.User;
import org.ut.server.omsserver.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageMapper {

    private UserRepository userRepository;
    public MessageDto toDto(ChatMessage chatMessage) {
        return MessageDto.builder()
                .id(chatMessage.getId() != null ? chatMessage.getId() : null)
                .chatId(chatMessage.getChatId() != null ? chatMessage.getChatId() : null)
                .receiverId(chatMessage.getReceiver() != null ? chatMessage.getReceiver().getId() : null)
                .senderId(chatMessage.getSender() != null ? chatMessage.getSender().getId() : null)
                .content(chatMessage.getContent())
                .type(chatMessage.getType())
                .build();
    }

    public ChatMessage toEntity(MessageDto dto) {
       // find sender by id
        User sender = userRepository.findUserById(dto.getSenderId())
                .orElseThrow(
                        () -> new RuntimeException("Sender not found with id " + dto.getSenderId())
                );

        // find receiver by id
        User receiver = userRepository.findUserById(dto.getReceiverId())
                .orElseThrow(
                        () -> new RuntimeException("Receiver not found with id " + dto.getReceiverId())
                );
        return ChatMessage.builder()
                .id(dto.getId())
                .chatId(dto.getChatId())
                .sender(sender)
                .receiver(receiver)
                .content(dto.getContent())
                .type(dto.getType())
                .build();
    }

    public List<MessageDto> toDtos(List<ChatMessage> chatMessages) {
        return chatMessages.stream().map(this::toDto).collect(Collectors.toList());
    }
}
