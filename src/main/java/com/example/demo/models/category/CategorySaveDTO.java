package com.example.demo.models.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaveDTO {
    private String name;
    private String description;
    private boolean active;
    private List<Long> serviceIds;
}
