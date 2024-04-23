package org.ut.server.omsserver.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.dto.NotificationDTO;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.ShopOwner;

import java.util.List;
import java.util.UUID;

@Service
public interface INotificationService {
    public void approveEmployeeRequest(UUID employeeId, Long requestId);
    public void rejectEmployeeRequest(UUID employeeId, Long requestId);
    // get all notifications for a user
    public List<NotificationDTO> getNotifications(UUID userId, Pageable pageable);
    public void markAsRead(UUID userId, Long notificationId);
    public void requestEmployee(UUID employeeId, UUID managerId);
    public void notifyOrderInfoToOwner(ShopOwner owner, ShopOwner employee, Order order, String message);
}
