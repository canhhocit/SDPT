package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;

public interface ProductInternalService {
    ProductInfoResponse getProductInfoByVariantId(Long variantId);
    Integer getStockQuantityByVariantId(Long variantId);
    long countProducts();
}
