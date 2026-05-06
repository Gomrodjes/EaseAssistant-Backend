package com.example.demo.services;

import java.util.List;

import com.example.demo.models.userServiceAssignment.UserServiceAssignmentResponseDTO;

public interface UserServiceAssignmentService {
    UserServiceAssignmentResponseDTO assignServiceToUser(Long userId, Long serviceId);

    void unassignServiceFromUser(Long userId, Long serviceId);

    List<UserServiceAssignmentResponseDTO> getActiveAssignmentsByUser(Long userId);

    List<UserServiceAssignmentResponseDTO> getActiveAssignmentsByService(Long serviceId);
}
