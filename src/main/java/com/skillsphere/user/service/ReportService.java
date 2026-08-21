package com.skillsphere.user.service;

import org.springframework.stereotype.Service;

import com.skillsphere.user.dto.UserReportResponse;
import com.skillsphere.user.entity.RoleName;
import com.skillsphere.user.repository.UserRepository;

@Service
public class ReportService {

    private final UserRepository userRepository;

    public ReportService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserReportResponse getUserReport() {

        long totalUsers =
                userRepository.count();

        long activeUsers =
                userRepository.findAll()
                        .stream()
                        .filter(user -> user.isActive())
                        .count();

        long inactiveUsers =
                totalUsers - activeUsers;

        long adminCount =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                user.getRoles()
                                    .stream()
                                    .anyMatch(role ->
                                        role.getName()
                                            == RoleName.ADMIN
                                    )
                        )
                        .count();

        long trainerCount =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                user.getRoles()
                                    .stream()
                                    .anyMatch(role ->
                                        role.getName()
                                            == RoleName.TRAINER
                                    )
                        )
                        .count();

        long studentCount =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                user.getRoles()
                                    .stream()
                                    .anyMatch(role ->
                                        role.getName()
                                            == RoleName.STUDENT
                                    )
                        )
                        .count();

        long hrCount =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                user.getRoles()
                                    .stream()
                                    .anyMatch(role ->
                                        role.getName()
                                            == RoleName.HR
                                    )
                        )
                        .count();

        long evaluatorCount =
                userRepository.findAll()
                        .stream()
                        .filter(user ->
                                user.getRoles()
                                    .stream()
                                    .anyMatch(role ->
                                        role.getName()
                                            == RoleName.EVALUATOR
                                    )
                        )
                        .count();

        return new UserReportResponse(
                totalUsers,
                activeUsers,
                inactiveUsers,
                adminCount,
                trainerCount,
                studentCount,
                hrCount,
                evaluatorCount
        );
    }
}