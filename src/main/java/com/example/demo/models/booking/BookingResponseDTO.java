package com.example.demo.models.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.example.demo.enums.StateBooking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private LocalDate dateBooking;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal totalPrice;
    private StateBooking state;
    private String clientNote;
    private Long addressId;
}
