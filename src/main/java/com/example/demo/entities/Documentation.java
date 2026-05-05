package com.example.demo.entities;

import java.time.LocalDate;

import com.example.demo.enums.TypeDocument;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documentations")
public class Documentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String originalFileName;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String storedFileName;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false)
    private String filePath;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDocument type;

    @NotNull
    @Column(nullable = false)
    private LocalDate uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;
}
