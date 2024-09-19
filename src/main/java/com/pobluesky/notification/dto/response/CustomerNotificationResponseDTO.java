package com.pobluesky.notification.dto.response;

import com.pobluesky.notification.entity.CustomerNotification;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CustomerNotificationResponseDTO(
    Long notificationId,
    Long userId,
    String notificationContents,
    boolean isRead,
    LocalDateTime createdDate

) {
    public static CustomerNotificationResponseDTO from(CustomerNotification customerNotification) {
        return CustomerNotificationResponseDTO.builder()
            .notificationId(customerNotification.getNotificationId())
            .userId(customerNotification.getUserId())
            .notificationContents(customerNotification.getNotificationContents())
            .isRead(customerNotification.getIsRead())
            .createdDate(customerNotification.getCreatedDate())
            .build();
    }
}
