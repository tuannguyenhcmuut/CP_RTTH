package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.ChatRoom;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySender_IdAndReceiver_Id(UUID senderId, UUID recipientId);
}
