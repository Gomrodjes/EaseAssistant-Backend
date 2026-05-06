package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Job;

@Repository("jobRepository")
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

}
