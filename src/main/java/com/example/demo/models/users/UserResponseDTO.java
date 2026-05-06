package com.example.demo.models.users;

import java.time.LocalDate;

import com.example.demo.enums.Gender;
import com.example.demo.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private String biography;
    private String phoneNumber;
    private UserRole role;
    private boolean isActive;
    private boolean isVerified;
    private boolean documentationVerified;
}
