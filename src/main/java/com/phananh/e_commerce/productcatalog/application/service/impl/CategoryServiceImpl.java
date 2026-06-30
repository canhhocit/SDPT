package com.phananh.e_commerce.productcatalog.application.service.impl;

import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.productcatalog.application.dto.query.CategorySearchQuery;
import com.phananh.e_commerce.productcatalog.domain.repository.CategoryRepository;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.productcatalog.application.mapper.CategoryMapper;
import com.phananh.e_commerce.productcatalog.domain.model.Category;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;
    CloudinaryService cloudinaryService;
    CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getEnabledCategories() {
        return categoryRepository.getEnabled()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getAllCategoriesBySearch(CategorySearchRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        CategorySearchQuery query = CategorySearchQuery.builder()
                .keyword(request.getKeyword() == null ? null : request.getKeyword().trim())
                .createdDateFrom(request.getCreatedDateFrom())
                .createdDateTo(request.getCreatedDateTo())
                .modifiedDateFrom(request.getModifiedDateFrom())
                .modifiedDateTo(request.getModifiedDateTo())
                .pageable(pageable)
                .build();

        return categoryRepository.getAllBySearch(query)
                .map(categoryMapper::toResponse);
    }

    @Override
    @Transactional
    public void createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getCategoryName().trim())) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Category category = Category.create(request.getCategoryName(), request.getCategoryDescription(), request.getImageUrl());

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateRequest request) {
        Category category = categoryRepository.getById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Update category info (name and description)
        category.updateName(request.getCategoryName());
        category.updateDescription(request.getCategoryDescription());

        // Update image handling using imageUrl semantics:
        // - imageUrl == null => keep existing image
        // - imageUrl is non-empty => set this new URL
        // - imageUrl is empty string ("") => remove existing image
        String imageUrl = request.getImageUrl();
        if (imageUrl != null) {
            if (!imageUrl.isBlank()) {
                // Set provided URL directly
                category.updateImage(imageUrl);
            } else {
                // empty string => remove existing image and delete from Cloudinary
                try {
                    cloudinaryService.deleteFileByUrl(imageUrl);
                } catch (Exception e) {
                    log.error("Error deleting category image", e);
                    throw new AppException(ErrorCode.FILE_DELETE_ERROR);
                }
                category.removeImage();
            }
        }

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategoryStatus(Long categoryId, String status) {
        Category category = categoryRepository.getById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (StringUtils.isBlank(status)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if ("ACTIVE".equalsIgnoreCase(status)) category.active();
        else if ("INACTIVE".equalsIgnoreCase(status)) category.inactive();

        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public String getCategoryNameById(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.getById(categoryId)
                .map(Category::getName)
                .orElse(null);
    }
}


