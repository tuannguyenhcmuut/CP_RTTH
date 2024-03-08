package org.ut.server.omsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.ut.server.omsserver.common.MessageCode;
import org.ut.server.omsserver.common.MessageConstants;
import org.ut.server.omsserver.config.JwtUtils;
import org.ut.server.omsserver.dto.NotificationDTO;
import org.ut.server.omsserver.dto.response.GenericResponseDTO;
import org.ut.server.omsserver.service.INotificationService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationController {

    //    jwtUtils
    private final JwtUtils jwtUtils;
    private final INotificationService notificationService;

    // get all messages for a user
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<List<NotificationDTO>> getNotifications(
            @RequestHeader("Authorization") String token
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        List<NotificationDTO> notifications = notificationService.getNotifications(userId);
        return GenericResponseDTO.<List<NotificationDTO>>builder()
                .data(notifications)
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_GET_NOTIFICATIONS)
                .timestamps(new Date())
                .build();
    }

    // mark a message as read
    @PutMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO<String> markAsRead(
            @RequestHeader("Authorization") String token,
            @PathVariable Long notificationId
    ) {
        UUID userId = UUID.fromString(jwtUtils.extractUserIdFromBearerToken(token));
        notificationService.markAsRead(userId, notificationId);
        return GenericResponseDTO.<String>builder()
                .data("Notification marked as read")
                .code(MessageCode.SUCCESS.toString())
                .message(MessageConstants.SUCCESS_MARK_AS_READ)
                .timestamps(new Date())
                .build();
    }
}
