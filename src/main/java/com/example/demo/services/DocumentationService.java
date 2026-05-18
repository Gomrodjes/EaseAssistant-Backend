package com.example.demo.services;

import java.util.List;

import com.example.demo.models.documentation.DocumentationFileDTO;
import com.example.demo.models.documentation.DocumentationResponseDTO;
import com.example.demo.models.documentation.DocumentationSaveDTO;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentationService {
    DocumentationResponseDTO createDocumentation(DocumentationSaveDTO documentationSaveDTO, MultipartFile file);

    List<DocumentationResponseDTO> getAllDocumentationByUser(Long userId);

    DocumentationFileDTO getDocumentationFile(Long documentationId);
}
