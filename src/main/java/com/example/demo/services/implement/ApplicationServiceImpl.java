package com.example.demo.services.implement;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Application;
import com.example.demo.entities.Documentation;
import com.example.demo.entities.User;
import com.example.demo.enums.StateApplication;
import com.example.demo.models.application.ApplicationResponseDTO;
import com.example.demo.models.application.ApplicationSaveDTO;
import com.example.demo.repositories.ApplicationRepository;
import com.example.demo.repositories.DocumentationRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ApplicationService;

import jakarta.transaction.Transactional;

@Service("applicationService")
public class ApplicationServiceImpl implements ApplicationService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("applicationRepository")
    private ApplicationRepository applicationRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("documentationRepository")
    private DocumentationRepository documentationRepository;

    @Override
    public List<ApplicationResponseDTO> getAllApplications() {
        List<ApplicationResponseDTO> applications = new ArrayList<>();
        for (Application application : applicationRepository.findAll()) {
            applications.add(toResponseDTO(application));
        }
        return applications;
    }

    @Override
    public ApplicationResponseDTO createApplication(ApplicationSaveDTO applicationSaveDTO) {
        User user = userRepository.findById(applicationSaveDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("The user does not exist"));

        if (user.getApplication() != null) {
            throw new IllegalArgumentException("The user already has an application");
        }

        List<Documentation> documentations = new ArrayList<>();
        if (applicationSaveDTO.getDocumentationsIDs() != null && !applicationSaveDTO.getDocumentationsIDs().isEmpty()) {
            documentations = documentationRepository.findAllById(applicationSaveDTO.getDocumentationsIDs());

            if (documentations.size() != applicationSaveDTO.getDocumentationsIDs().size()) {
                throw new IllegalArgumentException("One or more documentations do not exist");
            }

            for (Documentation documentation : documentations) {
                if (!documentation.getUser().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("All documentations must belong to the same user");
                }
            }
        }

        Application application = new Application();
        application.setState(StateApplication.PENDING);
        application.setUser(user);
        application.setDocumentations(new ArrayList<>());

        for (Documentation documentation : documentations) {
            documentation.setApplication(application);
            application.getDocumentations().add(documentation);
        }

        Application savedApplication = applicationRepository.save(application);
        user.setApplication(savedApplication);

        return toResponseDTO(savedApplication);
    }

    @Transactional
    @Override
    public ApplicationResponseDTO applicationAccepted(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The application not exist to accept"));

        application.setState(StateApplication.APPROVED);
        application.getUser().setDocumentationVerified(true);
        applicationRepository.save(application);
        return toResponseDTO(application);
    }

    @Transactional
    @Override
    public ApplicationResponseDTO applicationDenied(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The application not exist to deny"));

        application.setState(StateApplication.DENIED);
        application.getUser().setDocumentationVerified(false);
        applicationRepository.save(application);
        return toResponseDTO(application);
    }

    private ApplicationResponseDTO toResponseDTO(Application application) {
        return new ApplicationResponseDTO(
                application.getId(),
                application.getState().name(),
                application.getUser() != null ? application.getUser().getId() : null,
                application.getDocumentations().stream()
                        .map(Documentation::getOriginalFileName)
                        .collect(Collectors.toList()));
    }
}
