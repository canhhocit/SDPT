package com.phananh.e_commerce.product.presentation.dto.request.product.staff;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductVariantCreateRequest {

    @NotBlank(message = "SKU code is required")
    private String skuCode;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be >= 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity must be >= 0")
    private Integer stockQuantity;

    private String variantAvatarUrl;

    private List<String> variantImageUrls;

    private Map<String, String> attributes;
}
