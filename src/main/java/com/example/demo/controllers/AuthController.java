package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.auth.LoginRequestDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.models.users.UserResponseDTO;
import com.example.demo.models.users.UserRoleUpdateDTO;
import com.example.demo.models.users.UserSaveDTO;
import com.example.demo.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        try {
            String token = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(new ResponseApi<>(true, token, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserSaveDTO userSaveDTO) {
        try {
            UserResponseDTO createdUser = authService.register(userSaveDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseApi<>(true, createdUser, "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/select-account-type/{id}")
    public ResponseEntity<?> updateRoleAfterRegister(
            @PathVariable Long id,
            @RequestBody @Valid UserRoleUpdateDTO userRoleUpdateDTO) {
        try {
            UserResponseDTO updatedUser = authService.updateRoleAfterRegister(id, userRoleUpdateDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedUser, "User role updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
