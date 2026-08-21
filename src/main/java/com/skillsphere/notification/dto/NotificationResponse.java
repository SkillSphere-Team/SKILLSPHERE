package com.skillsphere.notification.dto;

import com.skillsphere.notification.entity.Notification;
import com.skillsphere.notification.entity.NotificationChannel;
import com.skillsphere.notification.entity.NotificationPriority;
import com.skillsphere.notification.entity.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationPriority priority;
    private NotificationChannel channel;
    private boolean read;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;


    public NotificationResponse(Notification notification) {

        this.id = notification.getId();
        this.userId = notification.getUserId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.priority = notification.getPriority();
        this.channel = notification.getChannel();
        this.read = notification.isRead();
        this.scheduledAt = notification.getScheduledAt();
        this.createdAt = notification.getCreatedAt();
    }


    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationType getType() {
        return type;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}