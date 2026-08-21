package com.skillsphere.notification.repository;

import com.skillsphere.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    // Get notifications that are available to the user
    // Immediate notifications: scheduledAt = null
    // Scheduled notifications: scheduledAt <= current time
    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.userId = :userId
        AND (
            n.scheduledAt IS NULL
            OR n.scheduledAt <= :now
        )
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findAvailableNotifications(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );


    // Get unread notifications that are currently available
    @Query("""
        SELECT n
        FROM Notification n
        WHERE n.userId = :userId
        AND n.read = false
        AND (
            n.scheduledAt IS NULL
            OR n.scheduledAt <= :now
        )
        ORDER BY n.createdAt DESC
    """)
    List<Notification> findAvailableUnreadNotifications(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );


    // Count unread notifications that are currently available
    @Query("""
        SELECT COUNT(n)
        FROM Notification n
        WHERE n.userId = :userId
        AND n.read = false
        AND (
            n.scheduledAt IS NULL
            OR n.scheduledAt <= :now
        )
    """)
    long countAvailableUnreadNotifications(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );


    // Existing methods can remain
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(
            Long userId
    );

    long countByUserIdAndReadFalse(Long userId);
}