package com.skillsphere.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillsphere.payment.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByStudentId(Long studentId);

}