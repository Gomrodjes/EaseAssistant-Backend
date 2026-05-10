package com.example.demo.models.users;

import com.example.demo.enums.UserRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateDTO {
    @NotNull
    private UserRole role;
}
