package com.example.demo.services;

import java.util.List;

import com.example.demo.models.users.UserResponseDTO;
import com.example.demo.models.users.UserUpdateDTO;

public interface UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);

    UserResponseDTO updateUserActiveStatus(Long id, boolean isActive);

    void deleteUser(Long id);
}
