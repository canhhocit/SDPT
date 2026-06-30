package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.core.util.ListUtils;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.command.ProductCreateCommand;
import com.phananh.e_commerce.product.application.dto.command.ProductVariantCreateCommand;
import com.phananh.e_commerce.product.application.dto.query.StaffProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.application.mapper.StaffProductMapper;
import com.phananh.e_commerce.product.application.service.StaffProductService;
import com.phananh.e_commerce.product.domain.model.*;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StaffProductServiceImpl implements StaffProductService {

    ProductRepository productRepository;
    StaffProductMapper staffProductMapper;
    CloudinaryService cloudinaryService;


    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProductsBySearch(StaffProductSearchRequest staffProductSearchRequest) {
        int page = PageUtils.getPageNumber(staffProductSearchRequest.getPage());
        int size = PageUtils.getPageSize(staffProductSearchRequest.getSize());
        String sortBy = PageUtils.getSortBy(staffProductSearchRequest.getSortBy());
        String sortType = PageUtils.getSortType(staffProductSearchRequest.getSortType());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        StaffProductSearchQuery query = StaffProductSearchQuery.builder()
                .keyword(staffProductSearchRequest.getKeyword() == null || staffProductSearchRequest.getKeyword().isBlank() ? null : staffProductSearchRequest.getKeyword().trim())
                .categoryId(staffProductSearchRequest.getCategoryId())
                .brandId(staffProductSearchRequest.getBrandId())
                .minPrice(staffProductSearchRequest.getMinPrice())
                .maxPrice(staffProductSearchRequest.getMaxPrice())
                .minRating(staffProductSearchRequest.getMinRating())
                .pageable(pageable)
                .build();

        return productRepository.getAllProductsBySearch(query)
                .map(staffProductMapper::toStaffProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getStaffProductById(Long id) {
        Product product = productRepository.getProductById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return staffProductMapper.toStaffProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getStaffProductVariantsByProductId(Long productId) {
        if (productRepository.getProductById(productId).isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productRepository.getVariantsByProductId(productId).stream()
                .map(staffProductMapper::toStaffProductVariantResponse)
                .toList();
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateRequest request) {
        Set<ProductVariant> variants = new HashSet<>();

        ProductCreateCommand productCreateCommand = ProductCreateCommand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .avatarUrl(request.getProductAvatarUrl())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .variants(variants)
                .build();

        Product product = Product.create(productCreateCommand);

        if (!ListUtils.isNullOrEmpty(request.getVariants())) {
            for (ProductCreateRequest.VariantCreateRequest variantRequest : request.getVariants()) {
                Set<VariantImage> images = new HashSet<>();
                Set<AttributeValue> attributeValues = new HashSet<>();
                ProductVariantCreateCommand variantCreateCommand = ProductVariantCreateCommand.builder()
                        .product(product)
                        .skuCode(variantRequest.getSkuCode())
                        .price(variantRequest.getPrice())
                        .stockQuantity(variantRequest.getStockQuantity())
                        .images(images)
                        .attributeValues(attributeValues)
                        .build();
                ProductVariant variant = ProductVariant.create(variantCreateCommand);

                // Add variant avatar (primary image) if URL is provided
                if (!StringUtils.isBlank(variantRequest.getVariantAvatarUrl())) {
                    images.add(VariantImage.create(variant, variantRequest.getVariantAvatarUrl(), true));
                }

                // Add variant gallery images if URLs are provided
                if (!ListUtils.isNullOrEmpty(variantRequest.getVariantImageUrls())) {
                    for (String imageUrl : variantRequest.getVariantImageUrls()) {
                        if (!StringUtils.isBlank(imageUrl)) {
                            images.add(VariantImage.create(variant, imageUrl, false));
                        }
                    }
                }

                // Process attributes if provided
                if (variantRequest.getAttributes() != null && !variantRequest.getAttributes().isEmpty()) {
                    for (Map.Entry<String, String> attribute : variantRequest.getAttributes().entrySet()) {
                        String name = attribute.getKey();
                        String value = attribute.getValue();

                        ProductAttribute productAttribute = productRepository.getProductAttributesByName(name)
                                .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
                        Set<AttributeValue> existingValues = productAttribute.getAttributeValues();
                        AttributeValue attributeValue = existingValues.stream()
                                .filter(v -> v.getValue().equals(value))
                                .findFirst()
                                .orElseGet(() -> {
                                    AttributeValue newValue = AttributeValue.create(value, productAttribute);
                                    productRepository.save(newValue);
                                    return newValue;
                                });

                        attributeValues.add(attributeValue);
                    }
                }

                variants.add(variant);
            }
        }


        productRepository.save(product);
    }

    @Override
    @Transactional
    public void createProductVariant(Long productId, ProductVariantCreateRequest request) {
        Product product = productRepository.getProductById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Set<VariantImage> images = new HashSet<>();
        Set<AttributeValue> attributeValues = new HashSet<>();

        ProductVariantCreateCommand variantCreateCommand = ProductVariantCreateCommand.builder()
                .product(product)
                .skuCode(request.getSkuCode())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .images(images)
                .attributeValues(attributeValues)
                .build();

        ProductVariant variant = ProductVariant.create(variantCreateCommand);

        if (!StringUtils.isBlank(request.getVariantAvatarUrl())) {
            images.add(VariantImage.create(variant, request.getVariantAvatarUrl(), true));
        }

        if (!ListUtils.isNullOrEmpty(request.getVariantImageUrls())) {
            for (String imageUrl : request.getVariantImageUrls()) {
                if (!StringUtils.isBlank(imageUrl)) {
                    images.add(VariantImage.create(variant, imageUrl, false));
                }
            }
        }

        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            for (Map.Entry<String, String> attribute : request.getAttributes().entrySet()) {
                String name = attribute.getKey();
                String value = attribute.getValue();

                ProductAttribute productAttribute = productRepository.getProductAttributesByName(name)
                        .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
                Set<AttributeValue> existingValues = productAttribute.getAttributeValues();
                AttributeValue attributeValue = existingValues.stream()
                        .filter(v -> v.getValue().equals(value))
                        .findFirst()
                        .orElseGet(() -> {
                            AttributeValue newValue = AttributeValue.create(value, productAttribute);
                            productRepository.save(newValue);
                            return newValue;
                        });

                attributeValues.add(attributeValue);
            }
        }

        product.addVariant(variant);
        productRepository.save(variant);
        productRepository.save(product);
    }


    @Override
    @Transactional
    public void updateProduct(ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.getProductById(productUpdateRequest.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateName(productUpdateRequest.getName());
        product.updateDescription(productUpdateRequest.getDescription());
        product.updateCategoryId(productUpdateRequest.getCategoryId());
        product.updateBrandId(productUpdateRequest.getBrandId());

        // Update image handling using imageUrl semantics:
        // - imageUrl == null => keep existing image
        // - imageUrl is non-empty => set this new URL
        // - imageUrl is empty string ("") => remove existing image
        if (productUpdateRequest.getProductAvatarUrl() != null) {
            product.updateAvatarUrl(productUpdateRequest.getProductAvatarUrl());
        }

        // Update variants
        if (!ListUtils.isNullOrEmpty(productUpdateRequest.getVariants())) {
            for (ProductUpdateRequest.VariantUpdateRequest variantRequest : productUpdateRequest.getVariants()) {
                try {
                    ProductVariant variant = productRepository.getVariantById(variantRequest.getVariantId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

                    // Update variant basic info
                    variant.updateSkuCode(variantRequest.getSkuCode());
                    variant.updatePrice(variantRequest.getPrice());
                    variant.updateStockQuantity(variantRequest.getStockQuantity());

                    // Delete images by ID
                    if (ListUtils.isNullOrEmpty(variantRequest.getVariantImageIdsToDelete())) {
                        List<VariantImage> variantImages = productRepository
                                .getVariantImagesById(variantRequest.getVariantImageIdsToDelete());
                        variant.removeListImages(variantImages);

                        List<String> imageUrls = variantImages.stream().map(VariantImage::getImageUrl).toList();
                        for (String url : imageUrls) cloudinaryService.deleteFileByUrl(url);
                    }

                    // Handle avatar URL update
                    // - imageUrl == null => keep existing image
                    // - imageUrl is non-empty => set this new URL
                    // - imageUrl is empty string ("") => remove existing image
                    if (variantRequest.getVariantAvatarUrl() != null) {
                        if (variantRequest.getVariantAvatarUrl().isBlank()) variant.removeAvatar();
                        else
                            variant.updateAvatar(VariantImage.create(variant, variantRequest.getVariantAvatarUrl(), true));
                    }

                    // Add new gallery images
                    if (!ListUtils.isNullOrEmpty(variantRequest.getVariantImagesUrlsToAdd())) {
                        variant.addListImage(variantRequest.getVariantImagesUrlsToAdd().stream()
                                .filter(url -> !StringUtils.isBlank(url))
                                .map(url -> VariantImage.create(variant, url, false))
                                .toList());
                    }

                    // Update variant attributes
                    if (variantRequest.getAttributes() != null && !variantRequest.getAttributes().isEmpty()) {
                        Set<AttributeValue> attributeValues = new HashSet<>();
                        for (Map.Entry<String, String> attribute : variantRequest.getAttributes().entrySet()) {
                            String attrName = attribute.getKey();
                            String attrValue = attribute.getValue();

                            ProductAttribute productAttribute = productRepository.getProductAttributesByName(attrName)
                                    .orElseThrow(() -> new AppException(ErrorCode.ATTRIBUTE_NOT_FOUND));
                            Set<AttributeValue> existingValues = productAttribute.getAttributeValues();
                            AttributeValue attributeValue = existingValues.stream()
                                    .filter(v -> v.getValue().equals(attrValue))
                                    .findFirst()
                                    .orElseGet(() -> {
                                        AttributeValue newValue = AttributeValue.create(attrValue, productAttribute);
                                        productRepository.save(newValue);
                                        return newValue;
                                    });
                            attributeValues.add(attributeValue);
                        }
                        variant.updateAttributeValues(attributeValues);
                    }

                    productRepository.save(variant);
                } catch (ObjectOptimisticLockingFailureException e) {
                    throw new AppException(ErrorCode.CONCURRENT_UPDATE_ERROR);
                }
            }
        }

        productRepository.save(product);
    }

    @Override
    public void updateProductStatus(Long productId, String status) {
        Product product = productRepository.getProductById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (StringUtils.isBlank(status)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        ProductStatus productStatus;
        try {
            productStatus = ProductStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        switch (productStatus) {
            case ACTIVE -> product.activate();
            case INACTIVE -> product.inactivate();
            case DRAFT -> product.isDraft();
        }

        productRepository.save(product);
    }

    @Override
    public void updateVariantStock(Long variantId, Integer stockQuantity) {
        try {
            ProductVariant variant = productRepository.getVariantById(variantId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

            if (stockQuantity == null || stockQuantity < 0) {
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }

            variant.updateStockQuantity(stockQuantity);
            productRepository.save(variant);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.CONCURRENT_UPDATE_ERROR);
        }
    }


}


