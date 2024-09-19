package com.pobluesky.notification.dto.request;

public record ManagerNotificationUpdateRequestDTO(
    Long notificationId,
    Long userId,
    String notificationContents,
    Boolean isRead
) {
}
