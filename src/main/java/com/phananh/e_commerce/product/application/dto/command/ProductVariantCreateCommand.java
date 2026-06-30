package com.phananh.e_commerce.product.application.dto.command;

import com.phananh.e_commerce.product.domain.model.AttributeValue;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.VariantImage;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class ProductVariantCreateCommand {
    private Product product;
    private String skuCode;
    private BigDecimal price;
    private Integer stockQuantity;
    private Set<VariantImage> images;
    private Set<AttributeValue> attributeValues;
}

