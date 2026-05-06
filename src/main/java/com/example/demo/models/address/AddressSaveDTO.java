package com.example.demo.models.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressSaveDTO {
    @NotBlank
    private String street;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String zipCode;

    @Size(max = 500)
    private String description;

    private boolean isPrimary;

    @NotNull
    private Long userId;
}
