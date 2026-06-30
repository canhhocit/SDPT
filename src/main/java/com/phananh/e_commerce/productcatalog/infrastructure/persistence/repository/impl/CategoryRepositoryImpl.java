package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.productcatalog.application.dto.query.CategorySearchQuery;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.productcatalog.domain.repository.CategoryRepository;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataCategoryRepository;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.specification.CategorySearchSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryRepositoryImpl implements CategoryRepository {

    SpringDataCategoryRepository springDataCategoryRepository;

    @Override
    public Page<Category> getAllBySearch(CategorySearchQuery categorySearchQuery) {
        Specification<Category> specification = Specification
                .where(CategorySearchSpecification.hasKeyword(categorySearchQuery.getKeyword()))
                .and(CategorySearchSpecification.createdAtFrom(categorySearchQuery.getCreatedDateFrom()))
                .and(CategorySearchSpecification.createdAtTo(categorySearchQuery.getCreatedDateTo()))
                .and(CategorySearchSpecification.modifiedAtFrom(categorySearchQuery.getModifiedDateFrom()))
                .and(CategorySearchSpecification.modifiedAtTo(categorySearchQuery.getModifiedDateTo()));

        return springDataCategoryRepository.findAll(specification, categorySearchQuery.getPageable());
    }

    @Override
    public List<Category> getEnabled() {
        return springDataCategoryRepository.findByIsEnabledTrue();
    }

    @Override
    public Category saveAndFlush(Category category) {
        return springDataCategoryRepository.saveAndFlush(category);
    }

    @Override
    public Category save(Category category) {
        return springDataCategoryRepository.save(category);
    }

    @Override
    public boolean existsByNameIgnoreCase(String normalizedName) {
        return springDataCategoryRepository.existsByNameIgnoreCase(normalizedName);
    }

    @Override
    public java.util.Optional<Category> getById(Long categoryId) {
        return springDataCategoryRepository.findById(categoryId);
    }
}
