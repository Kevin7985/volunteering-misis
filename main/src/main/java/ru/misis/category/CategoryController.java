package ru.misis.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.misis.category.dto.CategoryDto;
import ru.misis.category.dto.NewCategoryDto;
import ru.misis.category.dto.UpdateCategoryDto;
import ru.misis.utils.models.ListResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Validated
@Tag(name = "Categories", description = "Методы для работы с категориями событий")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание новой категории")
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @GetMapping
    @Operation(summary = "Поиск по названию категории")
    public ListResponse<CategoryDto> findCategories(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return categoryService.searchCategories(title, from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение категории по идентификатору")
    public CategoryDto getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление категории по идентификатору")
    public CategoryDto updateCategoryById(Authentication auth, @PathVariable UUID id, @RequestBody UpdateCategoryDto categoryDto) {
        return categoryService.updateCategoryById(auth, id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление категории по идентификатору")
    public void deleteCategoryById(Authentication auth, UUID id) {
        categoryService.deleteCategoryById(auth, id);
    }
}
