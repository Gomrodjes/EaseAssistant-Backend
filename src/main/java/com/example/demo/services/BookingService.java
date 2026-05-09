package com.example.demo.services;

import java.util.List;

import com.example.demo.models.booking.BookingCancellationDTO;
import com.example.demo.models.booking.BookingResponseDTO;
import com.example.demo.models.booking.BookingSaveDTO;
import com.example.demo.models.booking.BookingUpdateDTO;

public interface BookingService {
    BookingResponseDTO getBookingById(Long id);

    List<BookingResponseDTO> getAllBookingByUser(Long userId);

    BookingResponseDTO saveBooking(BookingSaveDTO bookingSaveDTO);

    BookingResponseDTO updateBooking(Long id, BookingUpdateDTO bookingUpdateDTO);

    void cancellationBooking(Long id, BookingCancellationDTO bookingCancellationDTO);
}
