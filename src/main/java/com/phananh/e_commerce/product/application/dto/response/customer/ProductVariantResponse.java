package com.phananh.e_commerce.product.application.dto.response.customer;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductVariantResponse {
    private Long variantId;
    private String skuCode;
    private BigDecimal price;
    private Integer stockQuantity;
    private List<String> imageUrls;
}
