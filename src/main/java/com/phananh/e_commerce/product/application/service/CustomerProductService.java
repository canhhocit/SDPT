package com.phananh.e_commerce.product.application.service;

import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.presentation.dto.request.product.customer.CustomerProductSearchRequest;
import org.springframework.data.domain.Page;

public interface CustomerProductService {

    Page<ProductSummaryResponse> getProductsActiveBySearch(CustomerProductSearchRequest customerProductSearchRequest);

    ProductDetailResponse getProductById(Long id);
}
