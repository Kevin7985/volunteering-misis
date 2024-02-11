package ru.misis.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CategoryDto {
    private final UUID id;
    private final String title;
}
