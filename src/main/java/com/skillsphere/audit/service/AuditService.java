package com.skillsphere.audit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillsphere.audit.entity.AuditLog;
import com.skillsphere.audit.repository.AuditLogRepository;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog log(Long userId, String action, String description) {

        AuditLog auditLog = new AuditLog(
                userId,
                action,
                description
        );

        return auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUserId(Long userId) {
        return auditLogRepository.findAll()
                .stream()
                .filter(log -> log.getUserId().equals(userId))
                .toList();
    }
}