package org.ut.server.omsserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.dto.NotificationDTO;
import org.ut.server.omsserver.model.EmployeeManagement;
import org.ut.server.omsserver.model.Notification;
import org.ut.server.omsserver.model.Order;
import org.ut.server.omsserver.model.ShopOwner;
import org.ut.server.omsserver.model.enums.NotificationType;
import org.ut.server.omsserver.model.enums.PermissionLevel;
import org.ut.server.omsserver.repo.EmployeeManagementRepository;
import org.ut.server.omsserver.repo.NotificationRepository;
import org.ut.server.omsserver.repo.ShopOwnerRepository;
import org.ut.server.omsserver.service.INotificationService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
        notification.setMessage(MessageConstants.SUCCESS_APPROVE_EMPLOYEE_REQUEST);
        notification.setReceiver(employeeManagementOptional.getManager());
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
        notification.setMessage(MessageConstants.SUCCESS_REJECT_EMPLOYEE_REQUEST);
        notification.setReceiver(employeeManagementOptional.getManager());
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
        notification.setMessage(String.format(MessageConstants.REQUEST_EMPLOYEE_MESSAGE, manager.getAccount().getUsername()));
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
            UUID userId,
            Pageable pageable
    ) {
       // get all notification for a user
        List<Notification> notis = notificationRepository.findByReceiverId(userId, pageable).orElse(Collections.emptyList());
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

    @Override
    public void deleteEmployeeManagement(UUID ownerId, UUID employeeId) {
        // existing code...
        // find employee by id
        ShopOwner employee = shopOwnerRepository.findShopOwnerById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));

        // mamager
        ShopOwner manager = shopOwnerRepository.findShopOwnerById(ownerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id " + ownerId));
        Notification notification = new Notification();
        notification.setMessage(MessageConstants.DELETE_EMPLOYEE_MESSAGE + manager.getAccount().getUsername());
        notification.setReceiver(employee);
        notification.setRead(false);
        notification.setType(
                NotificationType.DELETE_EMPLOYEE.toString()
        );
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Override
    public void updateEmployeeManagement(UUID employeeId, UUID managerId, Set<PermissionLevel> permissionLevels) {
        // existing code...
        // find employee by id
        ShopOwner employee = shopOwnerRepository.findShopOwnerById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id " + employeeId));

        // mamager
        ShopOwner manager = shopOwnerRepository.findShopOwnerById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with id " + managerId));
        Notification notification = new Notification();
        String permissionLevelsStr = permissionLevels.stream()
                .map(PermissionLevel::convertPermissionLevelStr)
                .collect(Collectors.joining(", "));
        notification.setMessage(String.format(
                MessageConstants.UPDATE_EMPLOYEE_MESSAGE,
                manager.getAccount().getUsername(),
                permissionLevelsStr
        ));
        notification.setReceiver(employee);
        notification.setRead(false);
        notification.setType(
                NotificationType.UPDATE_EMPLOYEE.toString()
        );
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

    }

    private String convertPermissionLevelStr(PermissionLevel permissionLevel) {
        if (permissionLevel == PermissionLevel.VIEW_ONLY) {
            return "Chỉ xem đơn hàng";
        } else if (permissionLevel == PermissionLevel.CREATE_ORDER) {
            return  "Tạo đơn hàng";
        } else if (permissionLevel == PermissionLevel.UPDATE_ORDER) {
            return "Cập nhật đơn hàng";
        } else if (permissionLevel == PermissionLevel.MANAGE_ORDER) {
            return "Quản lý đơn hàng";
        } else if (permissionLevel == PermissionLevel.CREATE_PRODUCT) {
            return "Tạo sản phẩm";
        } else if (permissionLevel == PermissionLevel.UPDATE_PRODUCT) {
            return "Cập nhật sản phẩm";
        } else if (permissionLevel == PermissionLevel.CREATE_RECEIVER) {
            return  "Tạo người nhận";
        } else if (permissionLevel == PermissionLevel.UPDATE_RECEIVER) {
            return "Cập nhật người nhận";
        } else if (permissionLevel == PermissionLevel.CREATE_STORE) {
            return  "Tạo cửa hàng";
        } else if (permissionLevel == PermissionLevel.UPDATE_STORE) {
            return "Cập nhật cửa hàng";
        } else {
            return "Unknown";
        }
    }
}
