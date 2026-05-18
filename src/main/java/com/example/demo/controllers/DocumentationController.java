package com.example.demo.controllers;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.enums.TypeDocument;
import com.example.demo.models.documentation.DocumentationFileDTO;
import com.example.demo.models.documentation.DocumentationResponseDTO;
import com.example.demo.models.documentation.DocumentationSaveDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.DocumentationService;

@RestController
@RequestMapping("/documentations")
public class DocumentationController {

    @Autowired
    @Qualifier("documentationService")
    private DocumentationService documentationService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllDocumentationByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseApi<>(true, documentationService.getAllDocumentationByUser(userId),
                        "Documentations retrieved successfully"));
    }

    @GetMapping("/file/{documentationId}")
    public ResponseEntity<?> getDocumentationFile(@PathVariable Long documentationId) {
        try {
            DocumentationFileDTO documentationFile = documentationService.getDocumentationFile(documentationId);
            Resource resource = documentationFile.getResource();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(documentationFile.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.inline()
                                    .filename(documentationFile.getOriginalFileName(), StandardCharsets.UTF_8)
                                    .build()
                                    .toString())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDocumentation(
            @RequestParam Long userId,
            @RequestParam TypeDocument type,
            @RequestParam MultipartFile file) {
        try {
            DocumentationSaveDTO documentationSaveDTO = new DocumentationSaveDTO(type, userId);
            DocumentationResponseDTO createdDocumentation = documentationService.createDocumentation(
                    documentationSaveDTO, file);
            return ResponseEntity.ok(
                    new ResponseApi<>(true, createdDocumentation, "Documentation created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
