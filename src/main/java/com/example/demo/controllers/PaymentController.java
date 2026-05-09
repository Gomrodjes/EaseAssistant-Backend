package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.payment.PaymentResponseDTO;
import com.example.demo.models.payment.PaymentSaveDTO;
import com.example.demo.models.payment.PaymentUpdateDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getPaymentByBookingId(@PathVariable Long bookingId) {
        PaymentResponseDTO payment = paymentService.getPaymentByBookingId(bookingId);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, "Payment not found for booking id: " + bookingId));
        }
        return ResponseEntity.ok(new ResponseApi<>(true, payment, "Payment retrieved successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody @Valid PaymentSaveDTO paymentSaveDTO) {
        try {
            PaymentResponseDTO createdPayment = paymentService.savePayment(paymentSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdPayment, "Payment created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/updateState/{id}")
    public ResponseEntity<?> updatePaymentState(@PathVariable Long id,
            @RequestBody @Valid PaymentUpdateDTO paymentUpdateDTO) {
        try {
            PaymentResponseDTO updatedPayment = paymentService.updateState(id, paymentUpdateDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedPayment, "Payment updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
