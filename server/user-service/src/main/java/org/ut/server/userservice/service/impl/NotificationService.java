package org.ut.server.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ut.server.userservice.model.EmployeeManagement;
import org.ut.server.userservice.model.Notification;
import org.ut.server.userservice.repo.EmployeeManagementRepository;
import org.ut.server.userservice.repo.NotificationRepository;
import org.ut.server.userservice.service.INotificationService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    private final EmployeeManagementRepository employeeManagementRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public void approveEmployeeRequest(UUID employeeId, Long requestId) {
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
    public void rejectEmployeeRequest(UUID employeeId, Long requestId) {
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


}
