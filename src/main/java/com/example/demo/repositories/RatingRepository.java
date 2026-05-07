package com.example.demo.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Rating;

@Repository("ratingRepository")
public interface RatingRepository extends JpaRepository<Rating, Serializable> {
    boolean existsByBookingIdAndAppraiserIdAndValuedId(Long bookingId, Long appraiserId, Long valuedId);
}
