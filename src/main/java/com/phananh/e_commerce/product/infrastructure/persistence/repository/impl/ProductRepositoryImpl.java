package com.phananh.e_commerce.product.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.product.application.dto.query.ProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.query.StaffProductSearchQuery;
import com.phananh.e_commerce.product.domain.model.*;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataAttributeValueRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductAttributeRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataProductVariantRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata.SpringDataVariantImageRepository;
import com.phananh.e_commerce.product.infrastructure.persistence.specification.ProductSearchSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ProductRepositoryImpl implements ProductRepository {

    SpringDataProductRepository springDataProductRepository;
    SpringDataProductVariantRepository springDataProductVariantRepository;
    SpringDataAttributeValueRepository springDataAttributeValueRepository;
    SpringDataProductAttributeRepository springDataProductAttributeRepository;
    SpringDataVariantImageRepository springDataVariantImageRepository;

    @Override
    public Page<Product> getProductsActiveBySearch(ProductSearchQuery productSearchQuery) {
        Pageable pageable = productSearchQuery.getPageable();

        Specification<Product> specification = Specification
                .where(ProductSearchSpecification.hasNameLike(productSearchQuery.getKeyword()))
                .and(ProductSearchSpecification.hasCategoryId(productSearchQuery.getCategoryId()))
                .and(ProductSearchSpecification.hasBrandId(productSearchQuery.getBrandId()))
                .and(ProductSearchSpecification.hasPriceBetween(productSearchQuery.getMinPrice(), productSearchQuery.getMaxPrice()))
                .and(ProductSearchSpecification.hasStatus("ACTIVE"));

        return springDataProductRepository.findAll(specification, pageable);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return springDataProductRepository.findById(id);
    }

    @Override
    public Page<Product> getAllProductsBySearch(StaffProductSearchQuery productSearchQuery) {
        Pageable pageable = productSearchQuery.getPageable();

        Specification<Product> specification = Specification
                .where(ProductSearchSpecification.hasNameLike(productSearchQuery.getKeyword()))
                .and(ProductSearchSpecification.hasCategoryId(productSearchQuery.getCategoryId()))
                .and(ProductSearchSpecification.hasBrandId(productSearchQuery.getBrandId()))
                .and(ProductSearchSpecification.hasPriceBetween(productSearchQuery.getMinPrice(), productSearchQuery.getMaxPrice()));

        return springDataProductRepository.findAll(specification, pageable);
    }

    @Override
    public List<ProductVariant> getVariantsByProductId(Long productId) {
        return springDataProductVariantRepository.findByProduct_Id(productId);
    }

    @Override
    public Optional<ProductVariant> getVariantById(Long id) {
        return springDataProductVariantRepository.findById(id);
    }

    @Override
    public Optional<ProductAttribute> getProductAttributesByName(String name) {
        return springDataProductAttributeRepository.findByName(name);
    }

    @Override
    public List<VariantImage> getVariantImagesById(List<Long> id) {
        return springDataVariantImageRepository.findByIdIn(id);
    }

    @Override
    public void save(Product product) {
        springDataProductRepository.save(product);
    }

    @Override
    public void save(AttributeValue attributeValue) {
        springDataAttributeValueRepository.save(attributeValue);
    }

    @Override
    public void save(ProductVariant variant) {
        springDataProductVariantRepository.save(variant);
    }

    @Override
    public long count() {
        return springDataProductRepository.count();
    }

    @Override
    public Optional<Product> findByProductVariants_Id(Long variantId) {
        return Optional.ofNullable(springDataProductRepository.findByVariants_Id(variantId));
    }
}

