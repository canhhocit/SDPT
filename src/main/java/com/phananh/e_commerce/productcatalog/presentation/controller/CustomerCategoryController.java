package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.application.dto.response.CategoryResponse;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Danh mục", description = "Các API quản lý danh mục sản phẩm")
public class CustomerCategoryController {

    CategoryService categoryService;

    @Operation(summary = "Lấy danh sách danh mục đang hoạt động", description = "Lấy tất cả danh mục sản phẩm đã kích hoạt")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getEnabledCategories() {
        List<CategoryResponse> categories = categoryService.getEnabledCategories();
        ApiResponse<List<CategoryResponse>> apiResponse = ApiResponse.<List<CategoryResponse>>builder()
                .message("Categories retrieved successfully")
                .result(categories)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}


