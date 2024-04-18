package org.ut.server.omsserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.dto.NotificationDTO;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.Notification;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.NotificationType;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.NotificationRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;
import org.ut.server.omsserver.service.INotificationService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final EmployeeManagementRepository employeeManagementRepository;
    private final NotificationRepository notificationRepository;
    private final ShopOwnerRepository shopOwnerRepository;

    @Override
    public void approveEmployeeRequest(UUID managerId, Long requestId) {
        // existing code...
        // find request
        EmployeeManagement employeeManagementOptional = employeeManagementRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Employee request not found with id " + requestId));


        Notification notification = new Notification();
        notification.setMessage("Your employee request has been approved.");
        notification.setReceiver(employeeManagementOptional.getManagerId());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void rejectEmployeeRequest(UUID managerId, Long requestId) {
        // existing code...
        EmployeeManagement employeeManagementOptional = employeeManagementRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Employee request not found with id " + requestId));


        Notification notification = new Notification();
        notification.setMessage("Your employee request has been rejected.");
        notification.setReceiver(employeeManagementOptional.getManagerId());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    // store notification for employee via request creation
    @Override
    public void requestEmployee(UUID employeeId, UUID managerId) {
        // existing code...
        // find employee by id
        ShopOwner employee = shopOwnerRepository.findShopOwnerById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));

        // mamager
        ShopOwner manager = shopOwnerRepository.findShopOwnerById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id " + managerId));
        Notification notification = new Notification();
        notification.setMessage("You have received a new employee request to become employee of " + manager.getAccount().getUsername());
        notification.setReceiver(employee);
        notification.setRead(false);
        notification.setType(
                NotificationType.EMPLOYEE_REQUEST.toString()
        );
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void notifyOrderInfoToOwner(ShopOwner owner, ShopOwner employee, Order order, String message) {
        // existing code...
        // find shop owner
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setReceiver(owner);
        notification.setRead(false);
        notification.setType(
                NotificationType.ORDER_INFO.toString()
        );
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDTO> getNotifications(
            UUID userId
    ) {
       // get all notification for a user
        List<Notification> notis = notificationRepository.findByReceiverId(userId).orElse(Collections.emptyList());
        return notis.stream().map(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setId(notification.getId());
            notificationDTO.setMessage(notification.getMessage());
            notificationDTO.setRead(notification.isRead());
            notificationDTO.setType(notification.getType());
            notificationDTO.setCreatedAt(notification.getCreatedAt());
            return notificationDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(UUID userId, Long notificationId) {
        // existing code...
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id " + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
