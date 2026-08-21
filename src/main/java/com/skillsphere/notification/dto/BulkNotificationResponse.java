package com.skillsphere.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BulkNotificationResponse {

    private String status;

    private int totalRecipients;

    private int notificationsCreated;

    private int emailsSent;

    private int emailsFailed;
}