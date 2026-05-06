package com.example.demo.models.userServiceAssignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceAssignmentResponseDTO {
    private Long id;
    private Long userId;
    private String userFullName;
    private Long serviceId;
    private String serviceName;
    private boolean active;
}
