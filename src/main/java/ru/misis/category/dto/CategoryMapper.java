package ru.misis.category.dto;

import org.springframework.stereotype.Component;
import ru.misis.category.model.Category;

@Component
public class CategoryMapper {
    public Category toCategory(NewCategoryDto categoryDto) {
        return new Category(
                null,
                categoryDto.getTitle()
        );
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getTitle()
        );
    }
}
