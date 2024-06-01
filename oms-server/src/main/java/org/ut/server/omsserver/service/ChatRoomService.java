package org.ut.server.omsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.model.ChatRoom;
import org.ut.server.omsserver.model.User;
import org.ut.server.omsserver.repo.ChatRoomRepository;
import org.ut.server.omsserver.repo.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public Optional<String> getChatRoomId(
            UUID senderId,
            UUID recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySender_IdAndReceiver_Id(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return  Optional.empty();
                });
    }

    private String createChatId(UUID senderId, UUID recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        // find sender by user id
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .sender(sender)
                .receiver(receiver)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .sender(receiver)
                .receiver(sender)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
