package com.example.demo.models.documentation;

import org.springframework.core.io.Resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentationFileDTO {
    private Resource resource;
    private String originalFileName;
    private String contentType;
}
