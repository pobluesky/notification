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
@Table(name = "manager_notification")
public class ManagerNotification extends Notification {

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public ManagerNotification(
        String notificationContents,
        Long userId
    ) {
        this.notificationContents = notificationContents;
        this.userId = userId;
    }

    public void updateIsRead(
        Boolean isRead
    ) {
        this.isRead = true;
    }
}
