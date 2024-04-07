package ru.misis.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.misis.category.model.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("select s from Category s " +
            "where upper(s.title) like upper(concat('%', ?1, '%'))")
    Page<Category> search(String text, Pageable pageable);

    @Query("select s from Category s " +
            "where upper(s.title) like upper(concat('%', ?1, '%'))")
    List<Category> search(String text);
}
