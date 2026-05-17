package com.example.demo.services;

import java.util.List;

import com.example.demo.models.userServiceAssignment.UserServiceAssignmentResponseDTO;

public interface UserServiceAssignmentService {
    UserServiceAssignmentResponseDTO assignServiceToUser(Long userId, Long categoryId);

    void unassignServiceFromUser(Long userId, Long categoryId);

    List<UserServiceAssignmentResponseDTO> getActiveAssignmentsByUser(Long userId);

    List<UserServiceAssignmentResponseDTO> getActiveAssignmentsByService(Long categoryId);
}
