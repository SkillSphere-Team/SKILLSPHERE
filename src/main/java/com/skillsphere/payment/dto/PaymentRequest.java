package com.skillsphere.payment.dto;

import java.math.BigDecimal;

import com.skillsphere.payment.entity.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Total fee is required")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Total fee must be greater than 0"
    )
    private BigDecimal totalFee;

    @NotNull(message = "Amount paid is required")
    @DecimalMin(
        value = "0.0",
        message = "Amount paid cannot be negative"
    )
    private BigDecimal amountPaid;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}