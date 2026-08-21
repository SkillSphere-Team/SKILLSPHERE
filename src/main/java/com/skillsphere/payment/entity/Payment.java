package com.skillsphere.payment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "student_id", nullable = false)
    private Long studentId;


    @Column(name = "total_fee", nullable = false)
    private BigDecimal totalFee;


    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;


    @Column(name = "balance", nullable = false)
    private BigDecimal balance;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;


    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;


    @PrePersist
    protected void onCreate() {

        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }

        calculateBalance();
        calculateStatus();
    }


    @PreUpdate
    protected void onUpdate() {

        calculateBalance();
        calculateStatus();
    }


    private void calculateBalance() {

        if (totalFee == null) {
            totalFee = BigDecimal.ZERO;
        }

        if (amountPaid == null) {
            amountPaid = BigDecimal.ZERO;
        }

        balance = totalFee.subtract(amountPaid);

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            balance = BigDecimal.ZERO;
        }
    }


    private void calculateStatus() {

        if (amountPaid == null ||
            amountPaid.compareTo(BigDecimal.ZERO) <= 0) {

            paymentStatus = PaymentStatus.PENDING;

        }
        else if (balance.compareTo(BigDecimal.ZERO) == 0) {

            paymentStatus = PaymentStatus.PAID;

        }
        else {

            paymentStatus = PaymentStatus.PARTIAL;
        }
    }
}