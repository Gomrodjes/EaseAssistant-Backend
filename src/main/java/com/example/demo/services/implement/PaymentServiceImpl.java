package com.example.demo.services.implement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Payment;
import com.example.demo.enums.StateBooking;
import com.example.demo.enums.StatePayment;
import com.example.demo.models.payment.PaymentResponseDTO;
import com.example.demo.models.payment.PaymentSaveDTO;
import com.example.demo.models.payment.PaymentUpdateDTO;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.PaymentRepository;
import com.example.demo.services.PaymentService;

import jakarta.transaction.Transactional;

@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("paymentRepository")
    private PaymentRepository paymentRepository;

    @Autowired
    @Qualifier("bookingRepository")
    private BookingRepository bookingRepository;

    @Transactional
    @Override
    public PaymentResponseDTO savePayment(PaymentSaveDTO paymentSaveDTO) {
        Booking booking = bookingRepository.findById(paymentSaveDTO.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking does not exist to create payment"));

        if (booking.getState() == StateBooking.CANCELED) {
            throw new IllegalArgumentException("Canceled bookings cannot have payments");
        }

        if (paymentRepository.existsByBooking_Id(booking.getId())) {
            throw new IllegalArgumentException("A payment already exists for this booking");
        }

        if (paymentSaveDTO.getAmount().compareTo(booking.getTotalPrice()) != 0) {
            throw new IllegalArgumentException("Payment amount must match booking total price");
        }

        if (paymentSaveDTO.getStripePaymentId() != null
                && !paymentSaveDTO.getStripePaymentId().isBlank()
                && paymentRepository.existsByStripePaymentId(paymentSaveDTO.getStripePaymentId())) {
            throw new IllegalArgumentException("Stripe payment id already exists");
        }

        BigDecimal platformCommission = calculatePlatformCommission(
                paymentSaveDTO.getAmount(),
                paymentSaveDTO.getCommissionPercentage());
        BigDecimal workerAmount = paymentSaveDTO.getAmount().subtract(platformCommission);

        Payment payment = new Payment();
        payment.setAmount(paymentSaveDTO.getAmount());
        payment.setCommissionPercentage(paymentSaveDTO.getCommissionPercentage());
        payment.setPlatformCommission(platformCommission);
        payment.setWorkerAmount(workerAmount);
        payment.setState(StatePayment.PENDING);
        payment.setStripePaymentId(normalizeStripePaymentId(paymentSaveDTO.getStripePaymentId()));
        payment.setBooking(booking);

        Payment savedPayment = paymentRepository.save(payment);
        booking.setPayment(savedPayment);

        return toResponseDTO(savedPayment);
    }

    @Override
    public PaymentResponseDTO getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBooking_Id(bookingId).orElse(null);
        if (payment == null) {
            return null;
        }
        return toResponseDTO(payment);
    }

    @Transactional
    @Override
    public PaymentResponseDTO updateState(Long id, PaymentUpdateDTO paymentUpdateDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment does not exist to update"));

        String stripePaymentId = normalizeStripePaymentId(paymentUpdateDTO.getStripePaymentId());
        if (stripePaymentId != null
                && !stripePaymentId.equals(payment.getStripePaymentId())
                && paymentRepository.existsByStripePaymentId(stripePaymentId)) {
            throw new IllegalArgumentException("Stripe payment id already exists");
        }

        payment.setState(paymentUpdateDTO.getState());
        if (stripePaymentId != null) {
            payment.setStripePaymentId(stripePaymentId);
        }

        Payment updatedPayment = paymentRepository.save(payment);
        return toResponseDTO(updatedPayment);
    }

    private PaymentResponseDTO toResponseDTO(Payment payment) {
        PaymentResponseDTO response = modelMapper.map(payment, PaymentResponseDTO.class);
        response.setBookingId(payment.getBooking().getId());
        return response;
    }

    private BigDecimal calculatePlatformCommission(BigDecimal amount, BigDecimal commissionPercentage) {
        return amount.multiply(commissionPercentage)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
    }

    private String normalizeStripePaymentId(String stripePaymentId) {
        if (stripePaymentId == null || stripePaymentId.isBlank()) {
            return null;
        }
        return stripePaymentId.trim();
    }
}
