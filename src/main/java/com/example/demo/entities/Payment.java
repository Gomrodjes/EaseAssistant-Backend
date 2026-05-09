package com.example.demo.entities;

import java.math.BigDecimal;

import com.example.demo.enums.StatePayment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "commission_percentage", nullable = false)
    private BigDecimal commissionPercentage;

    @Column(name = "platform_commission", nullable = false)
    private BigDecimal platformCommission;

    @Column(name = "worker_amount", nullable = false)
    private BigDecimal workerAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatePayment state;

    @Column(name = "stripe_payment_id", unique = true)
    private String stripePaymentId;

    @OneToOne
    @JoinColumn(name = "id_booking", nullable = false, unique = true)
    private Booking booking;
}
