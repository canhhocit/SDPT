package com.phananh.e_commerce.productcatalog.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getEnabledCategories();
    Page<CategoryResponse> getAllCategoriesBySearch(CategorySearchRequest categorySearchRequest);

    void createCategory(CategoryCreateRequest request);

    void updateCategory(CategoryUpdateRequest request);
    void updateCategoryStatus(Long categoryId, String status);

    /**
     * Lấy tên category từ category ID
     *
     * @param categoryId ID của category
     * @return Tên category hoặc null nếu không tìm thấy
     */
    String getCategoryNameById(Long categoryId);
}


