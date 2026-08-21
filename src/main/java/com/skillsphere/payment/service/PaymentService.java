package com.skillsphere.payment.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skillsphere.exception.ResourceNotFoundException;
import com.skillsphere.payment.dto.PaymentRequest;
import com.skillsphere.payment.entity.Payment;
import com.skillsphere.payment.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(PaymentRequest request) {

        Payment payment = Payment.builder()
                .studentId(request.getStudentId())
                .totalFee(request.getTotalFee())
                .amountPaid(request.getAmountPaid())
                .paymentMethod(request.getPaymentMethod())
                .build();

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {

        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        ));
    }

    public List<Payment> getPaymentsByStudent(Long studentId) {

        return paymentRepository.findByStudentId(studentId);
    }

    public void deletePayment(Long id) {

        Payment payment = getPaymentById(id);

        paymentRepository.delete(payment);
    }
}