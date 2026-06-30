package com.phananh.e_commerce.product.application.mapper;

import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.domain.model.AttributeValue;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.model.VariantImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CustomerProductMapper {

    default BigDecimal getMinPrice(Product product) {
        if (product.getVariants() == null || product.getVariants().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return product.getVariants().stream()
                .map(ProductVariant::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    default BigDecimal getMaxPrice(Product product) {
        if (product.getVariants() == null || product.getVariants().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return product.getVariants().stream()
                .map(ProductVariant::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "minPrice", expression = "java(getMinPrice(product))")
    @Mapping(source = "avatarUrl", target = "avatarUrl")
    ProductSummaryResponse toProductSummaryResponse(Product product);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "productDescription", source = "description")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "brandId", source = "brandId")
    @Mapping(target = "minPrice", expression = "java(getMinPrice(product))")
    @Mapping(target = "maxPrice", expression = "java(getMaxPrice(product))")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "variants", expression = "java(variantsToVariantResponseSet(product.getVariants()))")
    ProductDetailResponse toProductDetailResponse(Product product);

    @Mapping(source = "id", target = "variantId")
    @Mapping(source = "skuCode", target = "variantSkuCode")
    @Mapping(source = "price", target = "variantPrice")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(expression = "java(imagesToImageResponseSet(variant.getImages()))", target = "variantImageUrl")
    @Mapping(expression = "java(attributesToAttributeResponseSet(variant.getAttributeValues()))", target = "attributes")
    ProductDetailResponse.ProductVariantDetail toProductVariantResponse(ProductVariant variant);

    default Set<ProductDetailResponse.ProductVariantDetail> variantsToVariantResponseSet(Set<ProductVariant> variants) {
        if (variants == null) {
            return Set.of();
        }
        return variants.stream()
                .map(this::toProductVariantResponse)
                .collect(Collectors.toSet());
    }

    @Mapping(source = "id", target = "imageId")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "avatar", target = "avatar")
    ProductDetailResponse.ProductVariantDetail.Image toVariantImageResponse(VariantImage image);

    default Set<ProductDetailResponse.ProductVariantDetail.Image> imagesToImageResponseSet(Set<VariantImage> images) {
        if (images == null) {
            return Set.of();
        }
        return images.stream()
                .map(this::toVariantImageResponse)
                .collect(Collectors.toSet());
    }

    @Mapping(source = "attribute.id", target = "attributeId")
    @Mapping(source = "attribute.name", target = "attributeName")
    @Mapping(source = "value", target = "attributeValue")
    ProductDetailResponse.ProductVariantDetail.Attribute toAttributeResponse(AttributeValue attributeValue);

    default Set<ProductDetailResponse.ProductVariantDetail.Attribute> attributesToAttributeResponseSet(Set<AttributeValue> attributes) {
        if (attributes == null) {
            return Set.of();
        }
        return attributes.stream()
                .map(this::toAttributeResponse)
                .collect(Collectors.toSet());
    }
}


