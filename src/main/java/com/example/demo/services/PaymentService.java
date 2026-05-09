package com.example.demo.services;

import com.example.demo.models.payment.PaymentResponseDTO;
import com.example.demo.models.payment.PaymentSaveDTO;
import com.example.demo.models.payment.PaymentUpdateDTO;

public interface PaymentService {
    PaymentResponseDTO savePayment(PaymentSaveDTO paymentSaveDTO);

    PaymentResponseDTO updateState(Long id, PaymentUpdateDTO paymentUpdateDTO);

    PaymentResponseDTO getPaymentByBookingId(Long bookingId);
}
