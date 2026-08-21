package com.skillsphere.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsphere.audit.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}