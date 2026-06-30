package com.phananh.e_commerce.productcatalog.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
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
@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Thương hiệu", description = "Các API quản lý thương hiệu sản phẩm")
public class CustomerBrandController {

    BrandService brandService;

    @Operation(summary = "Lấy danh sách thương hiệu đang hoạt động", description = "Lấy tất cả thương hiệu sản phẩm đã kích hoạt")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getBrandsActive() {
        List<BrandResponse> brands = brandService.getBrandActive();
        ApiResponse<List<BrandResponse>> response = ApiResponse.<List<BrandResponse>>builder()
                .message("Get brands successfully")
                .result(brands)
                .build();
        return ResponseEntity.ok(response);
    }

}


