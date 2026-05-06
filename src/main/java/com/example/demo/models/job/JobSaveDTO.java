package com.example.demo.models.job;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSaveDTO {
    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotNull
    @Positive
    private BigDecimal price;

    @Min(1)
    private int durationMinutes;

    @NotBlank
    private String categoryName;
}
