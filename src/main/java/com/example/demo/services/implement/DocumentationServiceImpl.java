package com.example.demo.services.implement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Documentation;
import com.example.demo.entities.User;
import com.example.demo.models.documentation.DocumentationFileDTO;
import com.example.demo.models.documentation.DocumentationResponseDTO;
import com.example.demo.models.documentation.DocumentationSaveDTO;
import com.example.demo.repositories.DocumentationRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.DocumentationService;

@Service("documentationService")
public class DocumentationServiceImpl implements DocumentationService {

    private static final String UPLOAD_DIRECTORY = "uploads/documentations";

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("documentationRepository")
    private DocumentationRepository documentationRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public DocumentationResponseDTO createDocumentation(DocumentationSaveDTO documentationSaveDTO, MultipartFile file) {
        User user = userRepository.findById(documentationSaveDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist to create documentation"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The file is required");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!StringUtils.hasText(originalFileName)) {
            throw new IllegalArgumentException("The file name is not valid");
        }

        String storedFileName = UUID.randomUUID() + "_" + originalFileName;
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);
            Path targetPath = uploadPath.resolve(storedFileName).normalize();

            if (!targetPath.startsWith(uploadPath)) {
                throw new IllegalArgumentException("The file path is not valid");
            }

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            Documentation documentation = new Documentation();
            documentation.setOriginalFileName(originalFileName);
            documentation.setStoredFileName(storedFileName);
            documentation.setFilePath(targetPath.toString());
            documentation.setType(documentationSaveDTO.getType());
            documentation.setUploadDate(LocalDate.now());
            documentation.setUser(user);

            documentationRepository.save(documentation);

            DocumentationResponseDTO response = modelMapper.map(documentation, DocumentationResponseDTO.class);
            response.setUserId(user.getId());
            response.setApplicationId(
                    documentation.getApplication() != null ? documentation.getApplication().getId() : null);

            return response;
        } catch (IOException e) {
            throw new RuntimeException("The file could not be saved", e);
        }
    }

    @Override
    public List<DocumentationResponseDTO> getAllDocumentationByUser(Long userId) {
        List<DocumentationResponseDTO> documentations = new ArrayList<>();
        for (Documentation documentation : documentationRepository.findByUserId(userId)) {
            DocumentationResponseDTO response = modelMapper.map(documentation, DocumentationResponseDTO.class);
            response.setUserId(documentation.getUser().getId());
            response.setApplicationId(
                    documentation.getApplication() != null ? documentation.getApplication().getId() : null);
            documentations.add(response);
        }
        return documentations;
    }

    @Override
    public DocumentationFileDTO getDocumentationFile(Long documentationId) {
        Documentation documentation = documentationRepository.findById(documentationId)
                .orElseThrow(() -> new IllegalArgumentException("Documentation does not exist"));

        Path filePath = Paths.get(documentation.getFilePath()).toAbsolutePath().normalize();
        Resource resource = new PathResource(filePath);

        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalArgumentException("Documentation file is not available");
        }

        try {
            String contentType = Files.probeContentType(filePath);
            if (!StringUtils.hasText(contentType)) {
                contentType = "application/octet-stream";
            }

            return new DocumentationFileDTO(resource, documentation.getOriginalFileName(), contentType);
        } catch (IOException e) {
            throw new RuntimeException("The documentation file could not be read", e);
        }
    }

}
