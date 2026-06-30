package com.phananh.e_commerce.product.application.mapper;

import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import com.phananh.e_commerce.product.domain.model.VariantImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductInternalMapper {

	@Mapping(target = "productId", expression = "java(product.getId())")
	@Mapping(target = "productName", expression = "java(product.getName())")
	@Mapping(target = "productStatus", expression = "java(product.getStatus() == null ? null : product.getStatus().name())")
	@Mapping(target = "variantSkuCode", expression = "java(resolveVariant(product, variantId).getSkuCode())")
	@Mapping(target = "variantImageUrl", expression = "java(resolveVariantImageUrl(resolveVariant(product, variantId).getImages()))")
	@Mapping(target = "variantPrice", expression = "java(resolveVariant(product, variantId).getPrice())")
	@Mapping(target = "stockQuantity", expression = "java(resolveVariant(product, variantId).getStockQuantity())")
	ProductInfoResponse toResponse(Product product, Long variantId);

	@SuppressWarnings("unused")
	default ProductVariant resolveVariant(Product product, Long variantId) {
		return product.getVariants().stream()
				.filter(variant -> variant.getId().equals(variantId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Variant not found"));
	}

	@SuppressWarnings("unused")
	default String resolveVariantImageUrl(Set<VariantImage> images) {
		if (images == null || images.isEmpty()) {
			return null;
		}

		return images.stream()
				.filter(VariantImage::isAvatar)
				.findFirst()
				.map(VariantImage::getImageUrl)
				.orElse(null);
	}
}
