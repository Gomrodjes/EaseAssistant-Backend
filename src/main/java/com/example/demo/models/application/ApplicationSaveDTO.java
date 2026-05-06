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
public class ApplicationSaveDTO {
    private Long userId;
    private List<Long> documentationsIDs;
}
