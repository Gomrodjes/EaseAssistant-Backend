package com.example.demo.services;

import com.example.demo.models.rating.RatingResponseDTO;
import com.example.demo.models.rating.RatingSaveDTO;

public interface RatingService {
    RatingResponseDTO saveRating(RatingSaveDTO ratingSaveDTO);
}
