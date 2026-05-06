package com.example.demo.models.users;

import java.time.LocalDate;

import com.example.demo.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    private LocalDate dateOfBirth;
    private Gender gender;
    private String nationality;
    private String biography;

    @NotBlank
    private String phoneNumber;
}
