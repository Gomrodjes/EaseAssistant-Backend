package com.example.demo.models.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDTO {
    private String name;
    private String description;
    private boolean active;
}
