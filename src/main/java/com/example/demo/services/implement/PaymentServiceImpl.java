package com.example.demo.services.implement;

import org.springframework.stereotype.Service;

import com.example.demo.models.payment.PaymentResponseDTO;
import com.example.demo.models.payment.PaymentSaveDTO;
import com.example.demo.models.payment.PaymentUpdateDTO;
import com.example.demo.services.PaymentService;

@Service("paymenService")
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponseDTO savePayment(PaymentSaveDTO paymentSaveDTO) {
        return null;
    }

    @Override
    public PaymentResponseDTO getPaymentByBookingId(Long bookingId) {
        return null;
    }

    @Override
    public PaymentResponseDTO updateState(PaymentUpdateDTO paymentUpdateDTO) {
        return null;
    }
}
