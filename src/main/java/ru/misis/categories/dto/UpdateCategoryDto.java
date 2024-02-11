package ru.misis.categories.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.misis.categories.exceptions.CategoryValidation;

@Data
public class UpdateCategoryDto {
    private final String title;

    public UpdateCategoryDto(@JsonProperty("title") String title) {
        if (title != null && (title.length() < 4 || title.length() > 255)) {
            throw new CategoryValidation("Название категории не может быть меньше 4 символов и больше 255");
        }
        this.title = title;
    }
}
