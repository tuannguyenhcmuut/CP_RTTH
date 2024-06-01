package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.omsserver.model.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<List<Notification>> findByReceiverId(UUID receiverId, Pageable pageable);
    Optional<List<Notification>> findByReceiverId(UUID receiverId);
}
