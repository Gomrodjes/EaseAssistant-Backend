package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.rating.RatingResponseDTO;
import com.example.demo.models.rating.RatingSaveDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.RatingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    @Qualifier("ratingService")
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<?> createRating(@RequestBody @Valid RatingSaveDTO ratingSaveDTO) {
        try {
            RatingResponseDTO createdRating = ratingService.saveRating(ratingSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdRating, "Rating created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
