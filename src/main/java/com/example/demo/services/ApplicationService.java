package com.example.demo.services;

import java.util.List;

import com.example.demo.models.application.ApplicationResponseDTO;
import com.example.demo.models.application.ApplicationReviewDTO;
import com.example.demo.models.application.ApplicationSaveDTO;

public interface ApplicationService {
    List<ApplicationResponseDTO> getAllApplications();

    ApplicationResponseDTO createApplication(ApplicationSaveDTO applicationSaveDTO);

    ApplicationResponseDTO applicationAccepted(Long id, ApplicationReviewDTO applicationReviewDTO);

    ApplicationResponseDTO applicationDenied(Long id, ApplicationReviewDTO applicationReviewDTO);
}
