package com.phananh.e_commerce.productcatalog.domain.repository;

import com.phananh.e_commerce.productcatalog.application.dto.query.CategorySearchQuery;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository {
    Page<Category> getAllBySearch(CategorySearchQuery categorySearchQuery);
    List<Category> getEnabled();
    Category saveAndFlush(Category category);
    Category save(Category category);
    boolean existsByNameIgnoreCase(String normalizedName);
    Optional<Category> getById(Long categoryId);
}
