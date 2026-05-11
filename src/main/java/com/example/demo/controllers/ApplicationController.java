package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.application.ApplicationResponseDTO;
import com.example.demo.models.application.ApplicationReviewDTO;
import com.example.demo.models.application.ApplicationSaveDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.ApplicationService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    @Qualifier("applicationService")
    private ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<?> getAllApplication() {
        return ResponseEntity.ok(new ResponseApi<>(true, applicationService.getAllApplications(),
                "Applications retrieved successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createApplication(@RequestBody @Valid ApplicationSaveDTO applicationSaveDTO) {
        try {
            ApplicationResponseDTO createdApplication = applicationService.createApplication(applicationSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdApplication, "Application created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/approved/{id}")
    public ResponseEntity<?> getApprovedApplication(
            @PathVariable Long id,
            @RequestBody @Valid ApplicationReviewDTO applicationReviewDTO) {
        try {
            ApplicationResponseDTO approvedApplication = applicationService.applicationAccepted(id, applicationReviewDTO);
            return ResponseEntity
                    .ok(new ResponseApi<>(true, approvedApplication, "Application retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/denied/{id}")
    public ResponseEntity<?> getDeniedApplication(
            @PathVariable Long id,
            @RequestBody @Valid ApplicationReviewDTO applicationReviewDTO) {
        try {
            ApplicationResponseDTO deniedApplication = applicationService.applicationDenied(id, applicationReviewDTO);
            return ResponseEntity
                    .ok(new ResponseApi<>(true, deniedApplication, "Application retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

}
