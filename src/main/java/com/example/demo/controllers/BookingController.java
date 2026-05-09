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

import com.example.demo.models.booking.BookingCancellationDTO;
import com.example.demo.models.booking.BookingResponseDTO;
import com.example.demo.models.booking.BookingSaveDTO;
import com.example.demo.models.booking.BookingUpdateDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    @Qualifier("bookingService")
    private BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, "Booking not found with id: " + id));
        }
        return ResponseEntity.ok(new ResponseApi<>(true, booking, "Booking retrieved successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllBookingsByUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(new ResponseApi<>(true, bookingService.getAllBookingByUser(userId),
                    "Bookings retrieved successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody @Valid BookingSaveDTO bookingSaveDTO) {
        try {
            BookingResponseDTO createdBooking = bookingService.saveBooking(bookingSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdBooking, "Booking created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id,
            @RequestBody @Valid BookingUpdateDTO bookingUpdateDTO) {
        try {
            BookingResponseDTO updatedBooking = bookingService.updateBooking(id, bookingUpdateDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedBooking, "Booking updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id,
            @RequestBody @Valid BookingCancellationDTO bookingCancellationDTO) {
        try {
            bookingService.cancellationBooking(id, bookingCancellationDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, null, "Booking canceled successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
