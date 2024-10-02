package com.pobluesky.notification.dto.response;

import com.pobluesky.notification.entity.ManagerNotification;
import lombok.Builder;

@Builder
public record MobileNotificationResponseDTO(
    Long notificationId,
    Long userId,
    String notificationContents,
    boolean isRead
) {
    public static MobileNotificationResponseDTO from(ManagerNotification managerNotification) {
        return MobileNotificationResponseDTO.builder()
            .notificationId(managerNotification.getNotificationId())
            .userId(managerNotification.getUserId())
            .notificationContents(managerNotification.getNotificationContents())
            .isRead(managerNotification.getIsRead())
            .build();
    }
}
