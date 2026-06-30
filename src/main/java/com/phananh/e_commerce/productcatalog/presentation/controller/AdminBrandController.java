package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandCreateRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandSearchRequest;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.BrandUpdateRequest;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
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
@RequestMapping("admin/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Thương hiệu", description = "API quản trị thương hiệu sản phẩm")
public class AdminBrandController {

    BrandService brandService;

    @Operation(summary = "Tìm kiếm thương hiệu", description = "Tìm kiếm và phân trang danh sách thương hiệu")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BrandResponse>>> getBrandsBySearch(@Valid @ModelAttribute BrandSearchRequest request) {
        Page<BrandResponse> brands = brandService.getBrandsBySearch(request);
        ApiResponse<Page<BrandResponse>> response = ApiResponse.<Page<BrandResponse>>builder()
                .message("Get brands successfully")
                .result(brands)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Tạo thương hiệu mới", description = "Tạo thương hiệu sản phẩm mới trong hệ thống")
    @PostMapping
    public ResponseEntity<?> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        brandService.createBrand(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật thương hiệu", description = "Cập nhật thông tin thương hiệu sản phẩm")
    @PatchMapping("/update")
    public ResponseEntity<?> updateBrand(@Valid @RequestBody BrandUpdateRequest request) {
        brandService.updateBrand(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật trạng thái thương hiệu", description = "Kích hoạt hoặc vô hiệu hóa thương hiệu sản phẩm")
    @PatchMapping("/{brandId}/{status}")
    public ResponseEntity<?> updateBrandStatus(@PathVariable Long brandId, @PathVariable String status) {
        brandService.updateBrandStatus(brandId, status);
        return ResponseEntity.noContent().build();
    }
}

