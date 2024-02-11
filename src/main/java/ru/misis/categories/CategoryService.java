package ru.misis.categories;

import ru.misis.categories.dto.CategoryDto;
import ru.misis.categories.dto.NewCategoryDto;
import ru.misis.categories.dto.UpdateCategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    List<CategoryDto> searchCategories(String title, Integer from, Integer size);

    CategoryDto getCategoryById(UUID id);

    CategoryDto updateCategoryById(UUID id, UpdateCategoryDto categoryDto);

    void deleteCategoryById(UUID id);
}
