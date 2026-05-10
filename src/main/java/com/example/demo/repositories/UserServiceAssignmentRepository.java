package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.UserServiceAssignment;

@Repository("userServiceAssignmentRepository")
public interface UserServiceAssignmentRepository extends JpaRepository<UserServiceAssignment, Long> {
    Optional<UserServiceAssignment> findByUserIdAndJobId(Long userId, Long jobId);

    List<UserServiceAssignment> findByUserIdAndActiveTrue(Long userId);

    List<UserServiceAssignment> findByJobIdAndActiveTrue(Long serviceId);
}
