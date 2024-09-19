package com.pobluesky.notification.dto.response;

import com.pobluesky.notification.entity.ManagerNotification;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ManagerNotificationResponseDTO(
    Long notificationId,
    Long userId,
    String notificationContents,
    boolean isRead,
    LocalDateTime createdDate
) {
    public static ManagerNotificationResponseDTO from(ManagerNotification managerNotification) {
        return ManagerNotificationResponseDTO.builder()
            .notificationId(managerNotification.getNotificationId())
            .userId(managerNotification.getUserId())
            .notificationContents(managerNotification.getNotificationContents())
            .isRead(managerNotification.getIsRead())
            .createdDate(managerNotification.getCreatedDate())
            .build();
    }
}
