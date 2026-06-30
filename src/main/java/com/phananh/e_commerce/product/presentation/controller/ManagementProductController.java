package com.phananh.e_commerce.product.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.application.service.StaffProductService;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Sản phẩm", description = "Các API quản lý thông tin sản phẩm")
public class ManagementProductController {

    StaffProductService staffProductService;

    @Operation(summary = "Tìm kiếm sản phẩm", description = "Tìm kiếm và phân trang danh sách sản phẩm")
    @GetMapping("/product/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAllProductsBySearch(@ModelAttribute @Valid StaffProductSearchRequest staffProductSearchRequest) {
        Page<ProductResponse> products = staffProductService.getAllProductsBySearch(staffProductSearchRequest);
        ApiResponse<Page<ProductResponse>> response = ApiResponse.<Page<ProductResponse>>builder()
                .message("Get product successfully")
                .result(products)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy chi tiết sản phẩm", description = "Lấy thông tin chi tiết của một sản phẩm theo ID")
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getStaffProductById(@PathVariable Long id) {
        ProductResponse product = staffProductService.getStaffProductById(id);
        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .message("Get product successfully")
                .result(product)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy biến thể sản phẩm", description = "Lấy danh sách các biến thể của một sản phẩm")
    @GetMapping("/product/{productId}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getStaffProductVariantsByProductId(@PathVariable Long productId) {
        List<ProductVariantResponse> variants = staffProductService.getStaffProductVariantsByProductId(productId);
        ApiResponse<List<ProductVariantResponse>> response = ApiResponse.<List<ProductVariantResponse>>builder()
                .message("Get product variants successfully")
                .result(variants)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Tạo biến thể sản phẩm", description = "Tạo một biến thể mới cho sản phẩm")
    @PostMapping("/product/{productId}/variants")
    public ResponseEntity<?> createProductVariant(@PathVariable Long productId,
                                                  @RequestBody @Valid ProductVariantCreateRequest request) {
        staffProductService.createProductVariant(productId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Tạo sản phẩm mới", description = "Tạo một sản phẩm mới trong hệ thống")
    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        staffProductService.createProduct(productCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin chi tiết của sản phẩm")
    @PutMapping("/product/update")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        staffProductService.updateProduct(productUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật trạng thái sản phẩm", description = "Kích hoạt hoặc vô hiệu hóa sản phẩm")
    @PatchMapping("/product/{productId}/{status}")
    public ResponseEntity<?> updateProductStatus(@PathVariable Long productId, @PathVariable String status) {
        staffProductService.updateProductStatus(productId, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật số lượng tồn kho biến thể", description = "Cập nhật số lượng tồn kho của một biến thể sản phẩm")
    @PatchMapping("/variant/{variantId}/{stockQuantity}")
    public ResponseEntity<ApiResponse<Void>> updateVariantStock(@PathVariable Long variantId, @PathVariable Integer stockQuantity) {
        staffProductService.updateVariantStock(variantId, stockQuantity);
        return ResponseEntity.noContent().build();
    }
}
