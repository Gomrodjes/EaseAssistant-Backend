package com.example.demo.models.application;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReviewDTO {

    @Size(max = 2000, message = "The review message must not exceed 2000 characters")
    private String reviewMessage;
}
