package com.example.demo.models.documentation;

import com.example.demo.enums.TypeDocument;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationSaveDTO {
    @NotNull
    private TypeDocument type;

    @NotNull
    private Long userId;
}
