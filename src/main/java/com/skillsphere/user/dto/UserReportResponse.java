package com.skillsphere.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserReportResponse {

    private long totalUsers;

    private long activeUsers;

    private long inactiveUsers;

    private long adminCount;

    private long trainerCount;

    private long studentCount;

    private long hrCount;

    private long evaluatorCount;
}