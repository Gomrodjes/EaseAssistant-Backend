package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Job;
import com.example.demo.entities.User;
import com.example.demo.entities.UserServiceAssignment;
import com.example.demo.models.userServiceAssignment.UserServiceAssignmentResponseDTO;
import com.example.demo.repositories.JobRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.UserServiceAssignmentRepository;
import com.example.demo.services.UserServiceAssignmentService;

import jakarta.transaction.Transactional;

@Service("userServiceAssignmentService")
public class UserServiceAssignmentServiceImpl implements UserServiceAssignmentService {

    @Autowired
    @Qualifier("userServiceAssignmentRepository")
    private UserServiceAssignmentRepository userServiceAssignmentRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("jobRepository")
    private JobRepository jobRepository;

    @Transactional
    @Override
    public UserServiceAssignmentResponseDTO assignServiceToUser(Long userId, Long jobId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist with id: " + userId));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Service does not exist with id: " + jobId));

        UserServiceAssignment assignment = userServiceAssignmentRepository.findByUserIdAndJobId(userId, jobId)
                .orElseGet(UserServiceAssignment::new);

        assignment.setUser(user);
        assignment.setJob(job);
        assignment.setActive(true);

        UserServiceAssignment savedAssignment = userServiceAssignmentRepository.save(assignment);
        return toResponseDTO(savedAssignment);
    }

    @Transactional
    @Override
    public void unassignServiceFromUser(Long userId, Long serviceId) {
        UserServiceAssignment assignment = userServiceAssignmentRepository.findByUserIdAndJobId(userId, serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment does not exist"));

        if (!assignment.isActive()) {
            throw new IllegalArgumentException("Assignment is already inactive");
        }

        assignment.setActive(false);
        userServiceAssignmentRepository.save(assignment);
    }

    @Override
    public List<UserServiceAssignmentResponseDTO> getActiveAssignmentsByUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User does not exist with id: " + userId);
        }

        List<UserServiceAssignmentResponseDTO> assignments = new ArrayList<>();
        for (UserServiceAssignment assignment : userServiceAssignmentRepository.findByUserIdAndActiveTrue(userId)) {
            assignments.add(toResponseDTO(assignment));
        }
        return assignments;
    }

    @Override
    public List<UserServiceAssignmentResponseDTO> getActiveAssignmentsByService(Long jobId) {
        if (!jobRepository.findById(jobId).isPresent()) {
            throw new IllegalArgumentException("Service does not exist with id: " + jobId);
        }

        List<UserServiceAssignmentResponseDTO> assignments = new ArrayList<>();
        for (UserServiceAssignment assignment : userServiceAssignmentRepository
                .findByJobIdAndActiveTrue(jobId)) {
            assignments.add(toResponseDTO(assignment));
        }
        return assignments;
    }

    private UserServiceAssignmentResponseDTO toResponseDTO(UserServiceAssignment assignment) {
        return new UserServiceAssignmentResponseDTO(
                assignment.getId(),
                assignment.getUser().getId(),
                assignment.getUser().getFullName(),
                assignment.getJob().getId(),
                assignment.getJob().getName(),
                assignment.isActive());
    }
}
