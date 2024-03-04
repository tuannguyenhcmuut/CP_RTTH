package org.ut.server.userservice.service;

import org.springframework.stereotype.Service;
import org.ut.server.userservice.dto.NotificationDTO;

import java.util.List;
import java.util.UUID;

@Service
public interface INotificationService {
    public void approveEmployeeRequest(UUID employeeId, Long requestId);
    public void rejectEmployeeRequest(UUID employeeId, Long requestId);
    // get all notifications for a user
    public List<NotificationDTO> getNotifications(UUID userId);
    public void markAsRead(UUID userId, Long notificationId);
    public void requestEmployee(UUID employeeId, UUID managerId);
}
