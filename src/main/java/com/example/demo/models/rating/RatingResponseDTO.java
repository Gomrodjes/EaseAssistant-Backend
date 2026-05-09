package com.example.demo.models.rating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDTO {
    private Long id;
    private int score;
    private String comment;
    private Long bookingId;
    private Long appraiserId;
    private Long valuedId;
}
