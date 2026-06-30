package com.phananh.e_commerce.product.domain.repository;

import com.phananh.e_commerce.product.application.dto.query.ProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.query.StaffProductSearchQuery;
import com.phananh.e_commerce.product.domain.model.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Page<Product> getProductsActiveBySearch(ProductSearchQuery productSearchQuery);
    Optional<Product> getProductById(Long id);
    Page<Product> getAllProductsBySearch(StaffProductSearchQuery productSearchQuery);
    List<ProductVariant> getVariantsByProductId(Long productId);
    Optional<ProductVariant> getVariantById(Long id);
    Optional<ProductAttribute> getProductAttributesByName(String name);
    List<VariantImage> getVariantImagesById(List<Long> id);
    void save(Product product);
    void save(AttributeValue attributeValue);
    void save(ProductVariant variant);
    long count();

    Optional<Product> findByProductVariants_Id(Long variantId);
}

