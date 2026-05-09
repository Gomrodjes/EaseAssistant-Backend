package com.example.demo.services.implement;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Booking;
import com.example.demo.entities.Rating;
import com.example.demo.entities.User;
import com.example.demo.enums.StateBooking;
import com.example.demo.models.rating.RatingResponseDTO;
import com.example.demo.models.rating.RatingSaveDTO;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.RatingRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.RatingService;

import jakarta.transaction.Transactional;

@Service("ratingService")
public class RatingServiceImpl implements RatingService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("ratingRepository")
    private RatingRepository ratingRepository;

    @Autowired
    @Qualifier("bookingRepository")
    private BookingRepository bookingRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Transactional
    @Override
    public RatingResponseDTO saveRating(RatingSaveDTO ratingSaveDTO) {
        Booking booking = bookingRepository.findById(ratingSaveDTO.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking does not exist to create rating"));

        User appraiser = userRepository.findById(ratingSaveDTO.getAppraiserId())
                .orElseThrow(() -> new IllegalArgumentException("Appraiser user does not exist"));

        User valued = userRepository.findById(ratingSaveDTO.getValuedId())
                .orElseThrow(() -> new IllegalArgumentException("Valued user does not exist"));

        if (appraiser.getId().equals(valued.getId())) {
            throw new IllegalArgumentException("A user cannot rate themselves");
        }

        if (booking.getState() != StateBooking.COMPLETED) {
            throw new IllegalArgumentException("Only completed bookings can be rated");
        }

        boolean usersBelongToBooking = booking.getCustomer() != null
                && booking.getWorker() != null
                && ((booking.getCustomer().getId().equals(appraiser.getId())
                        && booking.getWorker().getId().equals(valued.getId()))
                        || (booking.getWorker().getId().equals(appraiser.getId())
                                && booking.getCustomer().getId().equals(valued.getId())));

        if (!usersBelongToBooking) {
            throw new IllegalArgumentException("Users do not belong to the booking being rated");
        }

        if (ratingRepository.existsByBookingIdAndAppraiserIdAndValuedId(
                booking.getId(),
                appraiser.getId(),
                valued.getId())) {
            throw new IllegalArgumentException("This rating already exists for the booking");
        }

        Rating rating = new Rating();
        rating.setScore(ratingSaveDTO.getScore());
        rating.setComment(ratingSaveDTO.getComment());
        rating.setBooking(booking);
        rating.setAppraiser(appraiser);
        rating.setValued(valued);

        Rating savedRating = ratingRepository.save(rating);

        int updatedNumberOfReviews = valued.getNumberOfReviews() + 1;
        double updatedAverageRating = ((valued.getAverageRating() * valued.getNumberOfReviews())
                + savedRating.getScore())
                / updatedNumberOfReviews;

        valued.setNumberOfReviews(updatedNumberOfReviews);
        valued.setAverageRating(updatedAverageRating);
        userRepository.save(valued);

        RatingResponseDTO response = modelMapper.map(rating, RatingResponseDTO.class);
        response.setBookingId(rating.getBooking().getId());
        response.setAppraiserId(rating.getAppraiser().getId());
        response.setValuedId(rating.getValued().getId());

        return response;
    }

}
