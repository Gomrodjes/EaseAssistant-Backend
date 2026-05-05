package com.example.demo.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.User;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Serializable> {
    Optional<User> findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
}
