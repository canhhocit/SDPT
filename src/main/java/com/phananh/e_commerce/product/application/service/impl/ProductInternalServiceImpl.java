package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.product.application.mapper.ProductInternalMapper;
import com.phananh.e_commerce.product.application.service.ProductInternalService;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductInternalServiceImpl implements ProductInternalService {

    ProductRepository productRepository;
    ProductInternalMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProductInfoResponse getProductInfoByVariantId(Long variantId) {
        Product product = productRepository.findByProductVariants_Id(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return mapper.toResponse(product, variantId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getStockQuantityByVariantId(Long variantId) {
        ProductVariant productVariant = productRepository.getVariantById(variantId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));
        return productVariant.getStockQuantity();
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }
}
