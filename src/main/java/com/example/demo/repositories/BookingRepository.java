package com.example.demo.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Booking;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, Serializable> {

}
