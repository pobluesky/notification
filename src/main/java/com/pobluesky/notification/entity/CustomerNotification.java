package com.pobluesky.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_notification")
public class CustomerNotification extends Notification {

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public CustomerNotification(
        String notificationContents,
        boolean isRead,
        Long userId
    ) {
        this.notificationContents = notificationContents;
        this.isRead = isRead;
        this.userId = userId;
    }

    public void updateIsRead(
        Boolean isRead
    ) {
        this.isRead = true;
    }
}
