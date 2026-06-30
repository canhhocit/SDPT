package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.staff.ProductResponse;
import com.phananh.e_commerce.product.application.dto.response.staff.ProductVariantResponse;
import com.phananh.e_commerce.product.presentation.dto.request.product.staff.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StaffProductService {


	// Staff - product detail
	ProductResponse getStaffProductById(Long id);

	// Staff - list variants by product
	List<ProductVariantResponse> getStaffProductVariantsByProductId(Long productId);

	// Staff - search all products (including inactive)
	Page<ProductResponse> getAllProductsBySearch(StaffProductSearchRequest staffProductSearchRequest);

	// Staff - create/update operations
	void createProduct(ProductCreateRequest productCreateRequest);

	void createProductVariant(Long productId, ProductVariantCreateRequest productVariantCreateRequest);

	void updateProduct(ProductUpdateRequest productUpdateRequest);

	void updateProductStatus(Long productId, String status);

	void updateVariantStock(Long variantId, Integer stockQuantity);
}


