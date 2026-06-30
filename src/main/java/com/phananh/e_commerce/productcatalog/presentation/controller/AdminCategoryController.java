package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategorySearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.category.CategoryUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Danh mục", description = "API quản trị danh mục sản phẩm")
public class AdminCategoryController {

    CategoryService categoryService;

    @Operation(summary = "Tìm kiếm danh mục", description = "Tìm kiếm và phân trang danh sách danh mục")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategoriesBySearch(@Valid @ModelAttribute CategorySearchRequest categorySearchRequest) {
        Page<CategoryResponse> categories = categoryService.getAllCategoriesBySearch(categorySearchRequest);
        ApiResponse<Page<CategoryResponse>> apiResponse = ApiResponse.<Page<CategoryResponse>>builder()
                .message("Categories retrieved successfully")
                .result(categories)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Tạo danh mục mới", description = "Tạo danh mục sản phẩm mới trong hệ thống")
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        categoryService.createCategory(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật danh mục", description = "Cập nhật thông tin danh mục sản phẩm")
    @PutMapping
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryUpdateRequest request) {
        categoryService.updateCategory(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật trạng thái danh mục", description = "Kích hoạt hoặc vô hiệu hóa danh mục sản phẩm")
    @PatchMapping("/{categoryId}/{status}")
    public ResponseEntity<?> updateCategoryStatus(@PathVariable Long categoryId, @PathVariable String status) {
        categoryService.updateCategoryStatus(categoryId, status);
        return ResponseEntity.noContent().build();
    }
}

