package org.ut.server.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ut.server.userservice.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
