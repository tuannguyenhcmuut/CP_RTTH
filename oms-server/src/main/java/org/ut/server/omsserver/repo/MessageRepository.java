package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.ChatMessage;
import org.ut.server.omsserver.model.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndReceiver(User sender, User receiver);
    List<ChatMessage> findByChatId(String chatId);
}
