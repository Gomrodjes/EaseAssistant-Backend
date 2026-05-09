package com.example.demo.models.booking;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaveDTO {
    @NotNull
    private LocalDate dateBooking;

    @NotNull
    private LocalTime startTime;

    @Size(max = 500)
    private String clientNote;

    @NotNull
    private Long customerId;

    @NotNull
    private Long workerId;

    @NotNull
    private Long jobId;

    @NotNull
    private Long addressId;
}
