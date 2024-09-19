package com.pobluesky.notification.dto.request;

public record CustomerNotificationUpdateRequestDTO(
    Long notificationId,
    Long userId,
    String notificationContents,
    Boolean isRead
) {
}
