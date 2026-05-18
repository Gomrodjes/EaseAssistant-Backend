package com.example.demo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.email.Email;
import com.example.demo.email.EmailService;
import com.example.demo.email.EmailTemplateService;
import com.example.demo.entities.User;
import com.example.demo.models.auth.LoginRequestDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.models.users.UserResponseDTO;
import com.example.demo.models.users.UserRoleUpdateDTO;
import com.example.demo.models.users.UserSaveDTO;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final String backendBaseUrl;

    public AuthController(
            AuthService authService,
            UserRepository userRepository,
            EmailService emailService,
            EmailTemplateService emailTemplateService,
            @Value("${app.backend.base-url}") String backendBaseUrl) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
        this.backendBaseUrl = backendBaseUrl;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        try {
            String token = authService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
            return ResponseEntity.ok(new ResponseApi<>(true, token, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserSaveDTO userSaveDTO) {
        try {
            UserResponseDTO createdUser = authService.register(userSaveDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseApi<>(true, createdUser, "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/select-account-type/{id}")
    public ResponseEntity<?> updateRoleAfterRegister(
            @PathVariable Long id,
            @RequestBody @Valid UserRoleUpdateDTO userRoleUpdateDTO) {
        try {
            UserResponseDTO updatedUser = authService.updateRoleAfterRegister(id, userRoleUpdateDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedUser, "User role updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PostMapping("/send/verification")
    public ResponseEntity<?> sendVerification(@RequestBody Map<String, String> body) {
        String emailAddress = body.get("email");
        if (emailAddress == null || emailAddress.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseApi<>(false, null, "Email is required"));
        }

        try {
            User user = userRepository.findByEmail(emailAddress.trim())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String url = backendBaseUrl + "/auth/verify/" + user.getId();
            String content = emailTemplateService.buildVerificationEmail(user.getFullName(), url);

            Email email = new Email(user.getEmail(), content, "Verificacion de cuenta");
            boolean sent = emailService.sendEmail(email);

            if (!sent) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseApi<>(false, null, "Verification email could not be sent"));
            }

            return ResponseEntity.ok(new ResponseApi<>(true, null, "Verification email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity<?> verifyUser(@PathVariable("id") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            user.setVerified(true);
            userRepository.save(user);

            return ResponseEntity.ok(new ResponseApi<>(true, null, "Cuenta verificada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @GetMapping("/verification-status/{id}")
    public ResponseEntity<?> getVerificationStatus(@PathVariable("id") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            UserResponseDTO userResponseDTO = new UserResponseDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getDateOfBirth(),
                    user.getGender(),
                    user.getNationality(),
                    user.getBiography(),
                    user.getPhoneNumber(),
                    user.getNumberOfReviews(),
                    user.getAverageRating(),
                    user.getRole(),
                    user.isActive(),
                    user.isVerified(),
                    user.isDocumentationVerified());

            return ResponseEntity.ok(
                    new ResponseApi<>(true, userResponseDTO, "Verification status retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

}
