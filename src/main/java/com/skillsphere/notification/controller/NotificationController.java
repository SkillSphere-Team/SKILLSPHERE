package com.skillsphere.notification.controller;

import com.skillsphere.notification.dto.BulkNotificationRequest;
import com.skillsphere.notification.dto.BulkNotificationResponse;
import com.skillsphere.notification.dto.CreateNotificationRequest;

import com.skillsphere.notification.dto.NotificationResponse;
import com.skillsphere.notification.entity.Notification;
import com.skillsphere.notification.service.NotificationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }


    // ==========================================
    // Get all notifications for a user
    // ==========================================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>>
    getUserNotifications(
            @PathVariable Long userId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getUserNotifications(userId)
                        .stream()
                        .map(NotificationResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(notifications);
    }


    // ==========================================
    // Get unread notifications
    // ==========================================

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<NotificationResponse>>
    getUnreadNotifications(
            @PathVariable Long userId) {

        List<NotificationResponse> notifications =
                notificationService
                        .getUnreadNotifications(userId)
                        .stream()
                        .map(NotificationResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(notifications);
    }


    // ==========================================
    // Get unread count
    // ==========================================

    @GetMapping("/user/{userId}/unread/count")
    public ResponseEntity<Long>
    getUnreadCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService
                        .getUnreadCount(userId)
        );
    }


    // ==========================================
    // Create notification
    // ==========================================

    @PostMapping
    public ResponseEntity<NotificationResponse>
    createNotification(
            @Valid @RequestBody
            CreateNotificationRequest request) {

        Notification notification =
                Notification.builder()
                        .userId(request.getUserId())
                        .title(request.getTitle())
                        .message(request.getMessage())
                        .type(request.getType())
                        .priority(request.getPriority())
                        .channel(request.getChannel())
                        .scheduledAt(
                                request.getScheduledAt()
                        )
                        .build();

        Notification saved =
                notificationService
                        .createNotification(notification);

        return ResponseEntity.ok(
                new NotificationResponse(saved)
        );
    }


    // ==========================================
    // Mark notification as read
    // ==========================================

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse>
    markAsRead(
            @PathVariable Long id) {

        Notification notification =
                notificationService
                        .markAsRead(id);

        return ResponseEntity.ok(
                new NotificationResponse(notification)
        );
    }
 // ==========================================
 // CREATE BULK NOTIFICATIONS
 // ==========================================

 @PostMapping("/bulk")
 public ResponseEntity<BulkNotificationResponse>
 createBulkNotifications(
         @Valid @RequestBody
         BulkNotificationRequest request) {

     BulkNotificationResponse response =
             notificationService
                     .createBulkNotifications(request);

     return ResponseEntity.ok(response);
 }

    // ==========================================
    // Delete notification
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteNotification(
            @PathVariable Long id) {

        notificationService
                .deleteNotification(id);

        return ResponseEntity.ok(
                "Notification deleted successfully"
        );
    }
}