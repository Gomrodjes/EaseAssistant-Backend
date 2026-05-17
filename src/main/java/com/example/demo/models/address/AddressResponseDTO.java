package com.example.demo.models.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
    private Long id;
    private String street;
    private String city;
    private String country;
    private String zipCode;
    private String description;
    private boolean isPrimary;

    private Long userId;
}
