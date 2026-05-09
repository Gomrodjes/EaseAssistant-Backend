package com.example.demo.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Payment;

@Repository("paymentRepository")
public interface PaymentRepository extends JpaRepository<Payment, Serializable> {
    Optional<Payment> findByBooking_Id(Long bookingId);

    boolean existsByBooking_Id(Long bookingId);

    boolean existsByStripePaymentId(String stripePaymentId);
}
