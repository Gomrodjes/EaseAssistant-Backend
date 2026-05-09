package com.example.demo.models.payment;

import java.math.BigDecimal;

import com.example.demo.enums.StatePayment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private Long id;
    private BigDecimal amount;
    private BigDecimal commissionPercentage;
    private BigDecimal platformCommission;
    private BigDecimal workerAmount;
    private StatePayment state;
    private String stripePaymentId;
    private Long bookingId;
}
