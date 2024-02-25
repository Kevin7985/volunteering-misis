package ru.misis.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.misis.category.dto.CategoryDto;
import ru.misis.category.dto.NewCategoryDto;
import ru.misis.category.dto.UpdateCategoryDto;
import ru.misis.category.model.Category;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.utils.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final MapperService mapperService;
    private final ValidationService validationService;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Category category = mapperService.toCategory(categoryDto);
        category = categoryRepository.save(category);

        log.info("Создана новая категория: " + category);
        return mapperService.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> searchCategories(String title, Integer from, Integer size) {
        List<CategoryDto> categoriesList = new ArrayList<>();

        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<Category> page;
        Pagination pager = new Pagination(from, size);

        for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
            pageable = PageRequest.of(i, pager.getPageSize(), sort);
            page = title == null ? categoryRepository.findAll(pageable) : categoryRepository.search(title, pageable);
            categoriesList.addAll(page.stream()
                    .map(mapperService::toCategoryDto)
                    .toList());
        }

        log.info(String.format("Поиск категорий (title = %s, from = %d, size = %d)", title, from, size));
        return categoriesList;
    }

    @Override
    public CategoryDto getCategoryById(UUID id) {
        Category category = validationService.validateCategory(id);

        log.info("Получение категории по id = " + id);
        return mapperService.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategoryById(Authentication auth, UUID id, UpdateCategoryDto categoryDto) {
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
            throw new Forbidden();
        }

        Category category = validationService.validateCategory(id);

        category.setTitle(categoryDto.getTitle() == null ? category.getTitle() : categoryDto.getTitle());
        category = categoryRepository.save(category);

        log.info("Обновлена категория: " + category);
        return mapperService.toCategoryDto(category);
    }

    @Override
    public void deleteCategoryById(Authentication auth, UUID id) {
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
            throw new Forbidden();
        }

        validationService.validateCategory(id);

        log.info("Удалена категория с id = " + id);
        categoryRepository.deleteById(id);
    }
}
