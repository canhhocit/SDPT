package com.phananh.e_commerce.product.application.dto.response.internal;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInfoResponse {
    private Long productId;
    private String productName;
    private String productStatus;
    private String variantSkuCode;
    private String variantImageUrl;
    private BigDecimal variantPrice;
    private Integer stockQuantity;
}
