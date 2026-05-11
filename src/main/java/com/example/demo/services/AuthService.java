package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.enums.UserRole;
import com.example.demo.models.users.UserResponseDTO;
import com.example.demo.models.users.UserRoleUpdateDTO;
import com.example.demo.models.users.UserSaveDTO;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtTokenProvider;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public String login(String email, String password) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenProvider.generateToken(authentication);
    }

    @Transactional
    public UserResponseDTO register(UserSaveDTO userSaveDTO) {
        String email = userSaveDTO.getEmail().trim();
        String phoneNumber = userSaveDTO.getPhoneNumber().trim();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already in use");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(userSaveDTO.getPassword()));
        user.setFullName(userSaveDTO.getFullName().trim());
        user.setDateOfBirth(userSaveDTO.getDateOfBirth());
        user.setGender(userSaveDTO.getGender());
        user.setNationality(userSaveDTO.getNationality());
        user.setBiography(userSaveDTO.getBiography());
        user.setPhoneNumber(phoneNumber);
        user.setRole(UserRole.CLIENT);
        user.setActive(true);
        user.setVerified(false);
        user.setDocumentationVerified(false);
        user.setAverageRating(0);
        user.setNumberOfReviews(0);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getDateOfBirth(),
                savedUser.getGender(),
                savedUser.getNationality(),
                savedUser.getBiography(),
                savedUser.getPhoneNumber(),
                savedUser.getRole(),
                savedUser.isActive(),
                savedUser.isVerified(),
                savedUser.isDocumentationVerified());
    }

    @Transactional
    public UserResponseDTO updateRoleAfterRegister(Long id, UserRoleUpdateDTO userRoleUpdateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The user not exist to update role"));

        user.setRole(userRoleUpdateDTO.getRole());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getDateOfBirth(),
                savedUser.getGender(),
                savedUser.getNationality(),
                savedUser.getBiography(),
                savedUser.getPhoneNumber(),
                savedUser.getRole(),
                savedUser.isActive(),
                savedUser.isVerified(),
                savedUser.isDocumentationVerified());
    }
}
