package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.UserServiceAssignmentService;

@RestController
@RequestMapping("/assignments")
public class UserServiceAssignmentController {

    @Autowired
    @Qualifier("userServiceAssignmentService")
    private UserServiceAssignmentService userServiceAssignmentService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAssignmentsByUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(new ResponseApi<>(true,
                    userServiceAssignmentService.getActiveAssignmentsByUser(userId),
                    "User assignments retrieved successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<?> getAssignmentsByService(@PathVariable Long serviceId) {
        try {
            return ResponseEntity.ok(new ResponseApi<>(true,
                    userServiceAssignmentService.getActiveAssignmentsByService(serviceId),
                    "Service assignments retrieved successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/user/{userId}/service/{serviceId}/activate")
    public ResponseEntity<?> activateAssignment(@PathVariable Long userId, @PathVariable Long serviceId) {
        try {
            return ResponseEntity.ok(new ResponseApi<>(true,
                    userServiceAssignmentService.assignServiceToUser(userId, serviceId),
                    "Assignment activated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/user/{userId}/service/{serviceId}/deactivate")
    public ResponseEntity<?> deactivateAssignment(@PathVariable Long userId, @PathVariable Long serviceId) {
        try {
            userServiceAssignmentService.unassignServiceFromUser(userId, serviceId);
            return ResponseEntity.ok(new ResponseApi<>(true, null, "Assignment deactivated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
