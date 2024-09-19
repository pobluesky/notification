package com.pobluesky.notification.dto.request;


import com.pobluesky.feign.Manager;
import com.pobluesky.notification.entity.ManagerNotification;

public record ManagerNotificationCreateRequestDTO(
    String notificationContents
) {
    public ManagerNotification toManagerNotificationEntity(Manager manager) {
        return ManagerNotification.builder()
            .userId(manager.getUserId())
            .notificationContents(notificationContents)
            .build();
    }
}
