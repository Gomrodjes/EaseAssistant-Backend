package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.response.ResponseApi;
import com.example.demo.models.users.UserResponseDTO;
import com.example.demo.models.users.UserUpdateDTO;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(new ResponseApi<>(true, userService.getAllUsers(), "Users retrieved successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedUser, "User updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/{id}/active/{isActive}")
    public ResponseEntity<?> updateUserActiveStatus(@PathVariable Long id, @PathVariable boolean isActive) {
        try {
            UserResponseDTO updatedUser = userService.updateUserActiveStatus(id, isActive);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedUser, "User status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ResponseApi<>(true, null, "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
