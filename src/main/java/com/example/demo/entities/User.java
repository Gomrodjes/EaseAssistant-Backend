package com.example.demo.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.enums.Gender;
import com.example.demo.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nationality;

    @Lob
    private String biography;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private int numberOfReviews = 0;

    @Column(nullable = false)
    private double averageRating = 0.0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(nullable = false)
    private boolean documentationVerified = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Application application;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Documentation> documentations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserServiceAssignment> userServiceAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Booking> customerBookings = new ArrayList<>();

    @OneToMany(mappedBy = "worker")
    private List<Booking> workerBookings = new ArrayList<>();

    @OneToMany(mappedBy = "appraiser")
    private List<Rating> appraiserRatings = new ArrayList<>();

    @OneToMany(mappedBy = "valued")
    private List<Rating> valuedRatings = new ArrayList<>();
}
