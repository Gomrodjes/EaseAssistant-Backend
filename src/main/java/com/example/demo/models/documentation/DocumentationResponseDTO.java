package com.example.demo.models.documentation;

import java.time.LocalDate;

import com.example.demo.enums.TypeDocument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationResponseDTO {
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private TypeDocument type;
    private LocalDate uploadDate;
    private Long userId;
    private Long applicationId;
}
