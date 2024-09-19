package com.pobluesky.notification.entity;

import com.pobluesky.global.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long notificationId;

    @Column(nullable = false)
    protected String notificationContents;

    @Column(nullable = false)
    protected Boolean isRead = false;
}
