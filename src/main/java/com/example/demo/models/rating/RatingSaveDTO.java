package com.example.demo.models.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingSaveDTO {
    @NotNull
    @Max(5)
    @Min(1)
    private int score;

    private String comment;

    @NotNull
    private Long bookingId;

    @NotNull
    private Long appraiserId;

    @NotNull
    private Long valuedId;
}
