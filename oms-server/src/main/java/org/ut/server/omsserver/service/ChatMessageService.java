package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.exception.EmployeeManagementException;
import org.ut.server.omsserver.model.ChatMessage;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.User;
import org.ut.server.omsserver.model.enums.EmployeeRequestStatus;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.MessageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EmployeeManagementRepository employeeManagementRepository;

    @Autowired
    private ChatRoomService chatRoomService;

    public void sendMessage(User sender, User receiver, String content) {
        // Check if the sender has the right to send a message to the receiver
        // For example, a user can only send a message to another user if they are friends
        Optional<EmployeeManagement> employManagement1 = employeeManagementRepository.findByEmployee_IdAndManager_IdAndApprovalStatus(sender.getId(), receiver.getId(), EmployeeRequestStatus.ACCEPTED);
        if (employManagement1.isEmpty()) {
            Optional<EmployeeManagement> employManagement2 = employeeManagementRepository.findByEmployee_IdAndManager_IdAndApprovalStatus(receiver.getId(), sender.getId(), EmployeeRequestStatus.ACCEPTED);
            if (employManagement2.isEmpty()) {
                throw new EmployeeManagementException(MessageConstants.ERROR_USER_NOT_HAS_OWNER);
            }
        }

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
    }

    public List<ChatMessage> getMessages(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSender().getId(), chatMessage.getReceiver().getId(), true)
                .orElseThrow(
                        () -> new RuntimeException("Cannot find chatId")
                ); // You can create your own dedicated exception
        chatMessage.setChatId(chatId);
        messageRepository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(UUID senderId, UUID recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());
    }
}