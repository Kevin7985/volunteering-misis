package ru.misis.category;

import org.springframework.security.core.Authentication;
import ru.misis.category.dto.CategoryDto;
import ru.misis.category.dto.NewCategoryDto;
import ru.misis.category.dto.UpdateCategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    List<CategoryDto> searchCategories(String title, Integer from, Integer size);

    CategoryDto getCategoryById(UUID id);

    CategoryDto updateCategoryById(Authentication auth, UUID id, UpdateCategoryDto categoryDto);

    void deleteCategoryById(Authentication auth, UUID id);
}
