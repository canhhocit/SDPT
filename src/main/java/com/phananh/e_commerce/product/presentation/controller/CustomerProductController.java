package com.phananh.e_commerce.product.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.application.service.CustomerProductService;
import com.phananh.e_commerce.product.presentation.dto.request.product.customer.CustomerProductSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Sản phẩm (customer)", description = "API khách hàng cho sản phẩm")
public class CustomerProductController {

    CustomerProductService customerProductService;

    @Operation(summary = "Tìm kiếm sản phẩm", description = "Tìm kiếm và phân trang danh sách sản phẩm đang bán")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductSummaryResponse>>> getProductsActiveBySearch(@ModelAttribute @Valid CustomerProductSearchRequest customerProductSearchRequest) {
        Page<ProductSummaryResponse> productSummaryResponses = customerProductService.getProductsActiveBySearch(customerProductSearchRequest);
        ApiResponse<Page<ProductSummaryResponse>> response = ApiResponse.<Page<ProductSummaryResponse>>builder()
                .message("Get list product successfully")
                .result(productSummaryResponses)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy chi tiết sản phẩm", description = "Lấy thông tin chi tiết của một sản phẩm theo ID bao gồm các biến thể")
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(@PathVariable Long id) {
        ProductDetailResponse product = customerProductService.getProductById(id);
        ApiResponse<ProductDetailResponse> response = ApiResponse.<ProductDetailResponse>builder()
                .message("Get product successfully")
                .result(product)
                .build();
        return ResponseEntity.ok(response);
    }
}

