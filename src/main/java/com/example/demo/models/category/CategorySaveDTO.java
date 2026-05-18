package com.example.demo.models.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaveDTO {
    @NotBlank
    private String name;

    @Size(max = 500)
    private String description;

    private boolean active;
}
