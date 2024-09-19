package com.pobluesky.notification.controller;

import com.pobluesky.global.util.ResponseFactory;
import com.pobluesky.global.util.model.JsonResult;

import com.pobluesky.notification.dto.request.CustomerNotificationCreateRequestDTO;
import com.pobluesky.notification.dto.request.CustomerNotificationUpdateRequestDTO;
import com.pobluesky.notification.dto.request.ManagerNotificationCreateRequestDTO;
import com.pobluesky.notification.dto.request.ManagerNotificationUpdateRequestDTO;
import com.pobluesky.notification.dto.response.CustomerNotificationResponseDTO;
import com.pobluesky.notification.dto.response.ManagerNotificationResponseDTO;
import com.pobluesky.notification.service.NotificationService;
import com.pobluesky.notification.service.NotificationType;

import io.swagger.v3.oas.annotations.Operation;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 고객사
    @Operation(summary = "고객사 계정별 읽지 않은 알림 전체 조회")
    @GetMapping("/customers/{userId}")
    public ResponseEntity<JsonResult> getCustomerNotificationsById(
        @RequestHeader("Authorization") String token,
        @PathVariable Long userId
    ) {
        List<?> response = notificationService.getNotificationsById(
            token,
            userId,
            NotificationType.CUSTOMER
        );

        List<CustomerNotificationResponseDTO> notifications = (List<CustomerNotificationResponseDTO>) response.get(0);
        Long totalElements = (Long) response.get(1);

        Map<String, Object> notification = new HashMap<>();
        notification.put("notifications", notifications);
        notification.put("totalElements", totalElements);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notification));
    }

    @Operation(summary = "고객사 계정별 읽은 알림 조회")
    @GetMapping("/customers/read/{userId}")
    public ResponseEntity<JsonResult> getRecentCustomerNotificationById(
        @RequestHeader("Authorization") String token,
        @PathVariable Long userId
    ) {

        List<?> notificationById = notificationService.getRecentNotifications(
            token,
            userId,
            NotificationType.CUSTOMER
        );

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notificationById));
    }

    @Operation(summary = "고객사 계정별 알림 전송")
    @PostMapping("/customers/{userId}")
    public ResponseEntity<JsonResult> createCustomerNotification(
        @RequestHeader("Authorization") String token,
        @RequestBody CustomerNotificationCreateRequestDTO dto,
        @PathVariable Long userId) {

        CustomerNotificationResponseDTO notification =
            (CustomerNotificationResponseDTO) notificationService.createNotification(
                token,
                dto,
                userId,
                NotificationType.CUSTOMER
            );

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notification));
    }

    @Operation(summary = "고객사 알림 상태 변경(안 읽음 -> 읽음)")
    @PutMapping("/customers/{notificationId}")
    public ResponseEntity<JsonResult> updateCustomerNotificationIsRead(
        @RequestHeader("Authorization") String token,
        @PathVariable Long notificationId,
        @RequestBody CustomerNotificationUpdateRequestDTO dto
    ) {
        CustomerNotificationResponseDTO notification =
            (CustomerNotificationResponseDTO) notificationService.updateNotificationIsRead(
                token,
                notificationId,
                dto,
                NotificationType.CUSTOMER
            );

        return ResponseEntity.status(HttpStatus.OK)
           .body(ResponseFactory.getSuccessJsonResult(notification));
    }


    // 담당자
    @Operation(summary = "담당자 계정별 읽지 않은 알림 전체 조회")
    @GetMapping("/managers/{userId}")
    public ResponseEntity<JsonResult> getAllManagerNotifications(
        @RequestHeader("Authorization") String token,
        @PathVariable Long userId
    ) {
        List<?> response = notificationService.getNotificationsById(
            token,
            userId,
            NotificationType.MANAGER
        );

        List<ManagerNotificationResponseDTO> notifications = (List<ManagerNotificationResponseDTO>) response.get(0);
        Long totalElements = (Long) response.get(1);

        Map<String, Object> notification = new HashMap<>();
        notification.put("notifications", notifications);
        notification.put("totalElements", totalElements);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notification));
    }

    @Operation(summary = "담당자 계정별 읽은 알림 조회")
    @GetMapping("/managers/read/{userId}")
    public ResponseEntity<JsonResult> getRecentManagerNotificationById(
        @RequestHeader("Authorization") String token,
        @PathVariable Long userId
    ) {
        List<?> notificationById = notificationService.getRecentNotifications(
            token,
            userId,
            NotificationType.MANAGER
        );

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notificationById));
    }

    @Operation(summary = "담당자 계정별 알림 전송")
    @PostMapping("/managers/{userId}")
    public ResponseEntity<JsonResult> createManagerNotification(
        @RequestHeader("Authorization") String token,
        @RequestBody ManagerNotificationCreateRequestDTO dto,
        @PathVariable Long userId
    ) {
        ManagerNotificationResponseDTO notification =
            (ManagerNotificationResponseDTO) notificationService.createNotification(
                token,
                dto,
                userId,
                NotificationType.MANAGER
            );

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notification));
    }

    @Operation(summary = "담당자 알림 상태 변경(안 읽음 -> 읽음)")
    @PutMapping("/managers/{notificationId}")
    public ResponseEntity<JsonResult> updateManagerNotificationIsRead(
        @RequestHeader("Authorization") String token,
        @PathVariable Long notificationId,
        @RequestBody ManagerNotificationUpdateRequestDTO dto
    ) {
        ManagerNotificationResponseDTO notification =
            (ManagerNotificationResponseDTO) notificationService.updateNotificationIsRead(
                token,
                notificationId,
                dto,
                NotificationType.MANAGER
            );

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseFactory.getSuccessJsonResult(notification));
    }
}
