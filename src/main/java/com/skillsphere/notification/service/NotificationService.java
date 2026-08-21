package com.skillsphere.notification.service;

import com.skillsphere.notification.dto.BulkNotificationRequest;
import com.skillsphere.notification.dto.BulkNotificationResponse;
import com.skillsphere.notification.entity.Notification;
import com.skillsphere.notification.repository.NotificationRepository;
import com.skillsphere.user.entity.User;
import com.skillsphere.user.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            EmailService emailService) {

        this.notificationRepository =
                notificationRepository;

        this.userRepository =
                userRepository;

        this.emailService =
                emailService;
    }


    // ==========================================
    // GET USER NOTIFICATIONS
    // ==========================================

    public List<Notification> getUserNotifications(
            Long userId) {

        return notificationRepository
                .findAvailableNotifications(
                        userId,
                        LocalDateTime.now()
                );
    }


    // ==========================================
    // GET UNREAD NOTIFICATIONS
    // ==========================================

    public List<Notification> getUnreadNotifications(
            Long userId) {

        return notificationRepository
                .findAvailableUnreadNotifications(
                        userId,
                        LocalDateTime.now()
                );
    }


    // ==========================================
    // GET UNREAD COUNT
    // ==========================================

    public long getUnreadCount(Long userId) {

        return notificationRepository
                .countAvailableUnreadNotifications(
                        userId,
                        LocalDateTime.now()
                );
    }


    // ==========================================
    // CREATE NOTIFICATION
    // ==========================================

    public Notification createNotification(
            Notification notification) {

        Notification saved =
                notificationRepository.save(
                        notification
                );

        // Send email for EMAIL or BOTH
        if (notification.getChannel() != null
                && (notification.getChannel().name().equals("EMAIL")
                || notification.getChannel().name().equals("BOTH"))) {

            User user =
                    userRepository.findById(
                            notification.getUserId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "User not found with id: "
                                            + notification.getUserId()
                            )
                    );

            emailService.sendEmail(
                    user.getEmail(),
                    notification.getTitle(),
                    notification.getMessage()
            );
        }

        return saved;
    }


    // ==========================================
    // MARK NOTIFICATION AS READ
    // ==========================================

    public Notification markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        notification.setRead(true);

        return notificationRepository.save(
                notification
        );
    }
    
 // ==========================================
 // CREATE BULK NOTIFICATIONS
 // ==========================================

 public BulkNotificationResponse createBulkNotifications(
         BulkNotificationRequest request) {

     int emailsSent = 0;
     int emailsFailed = 0;

     List<Notification> notifications =
             request.getUserIds()
                     .stream()
                     .map(userId -> Notification.builder()
                             .userId(userId)
                             .title(request.getTitle())
                             .message(request.getMessage())
                             .type(request.getType())
                             .priority(request.getPriority())
                             .channel(request.getChannel())
                             .scheduledAt(request.getScheduledAt())
                             .build())
                     .toList();

     List<Notification> savedNotifications =
             notificationRepository.saveAll(
                     notifications
             );

     // ==========================================
     // SEND BULK EMAILS
     // ==========================================

     if (request.getChannel() != null
             && (request.getChannel().name().equals("EMAIL")
             || request.getChannel().name().equals("BOTH"))) {

         for (Long userId : request.getUserIds()) {

             try {

                 User user =
                         userRepository.findById(userId)
                                 .orElseThrow(() ->
                                         new RuntimeException(
                                                 "User not found with id: "
                                                         + userId
                                         )
                                 );

                 emailService.sendEmail(
                         user.getEmail(),
                         request.getTitle(),
                         request.getMessage()
                 );

                 emailsSent++;

             } catch (Exception e) {

                 emailsFailed++;
             }
         }
     }

     String status;

     if (emailsFailed == 0) {
         status = "SUCCESS";
     } else if (emailsSent > 0) {
         status = "PARTIALLY_COMPLETED";
     } else {
         status = "FAILED";
     }

     return new BulkNotificationResponse(
             status,
             request.getUserIds().size(),
             savedNotifications.size(),
             emailsSent,
             emailsFailed
     );
 }

    // ==========================================
    // DELETE NOTIFICATION
    // ==========================================

    public void deleteNotification(Long id) {

        if (!notificationRepository.existsById(id)) {

            throw new RuntimeException(
                    "Notification not found with id: " + id
            );
        }

        notificationRepository.deleteById(id);
    }
}