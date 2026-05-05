package com.example.demo.models.application;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDTO {
    private Long id;
    private String state;
    private Long userId;
    private List<String> documentationsNames;
}
