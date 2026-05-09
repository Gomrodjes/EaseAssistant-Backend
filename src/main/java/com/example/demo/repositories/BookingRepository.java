package com.example.demo.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Booking;
import com.example.demo.enums.StateBooking;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, Serializable> {
    List<Booking> findByCustomer_IdOrWorker_Id(Long customerId, Long workerId);

    List<Booking> findByDateBookingAndStateNot(LocalDate dateBooking, StateBooking state);
}
