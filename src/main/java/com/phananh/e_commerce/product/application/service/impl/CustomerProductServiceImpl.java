package com.phananh.e_commerce.product.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.query.ProductSearchQuery;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductDetailResponse;
import com.phananh.e_commerce.product.application.dto.response.customer.ProductSummaryResponse;
import com.phananh.e_commerce.product.application.mapper.CustomerProductMapper;
import com.phananh.e_commerce.product.application.service.CustomerProductService;
import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.repository.ProductRepository;
import com.phananh.e_commerce.product.presentation.dto.request.product.customer.CustomerProductSearchRequest;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import com.phananh.e_commerce.productcatalog.application.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerProductServiceImpl implements CustomerProductService {

    ProductRepository productRepository;
    CustomerProductMapper customerProductMapper;
    BrandService brandService;
    CategoryService categoryService;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummaryResponse> getProductsActiveBySearch(CustomerProductSearchRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        ProductSearchQuery query = ProductSearchQuery.builder()
                .keyword(StringUtils.isBlank(request.getKeyword()) ? null : request.getKeyword().trim())
                .categoryId(request.getCategoryId())
                .brandId(request.getBrandId())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .pageable(pageable)
                .build();

        Page<Product> products = productRepository.getProductsActiveBySearch(query);
        return products.map(customerProductMapper::toProductSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(Long id) {
        Product product = productRepository.getProductById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getVariants() == null || product.getVariants().isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        }

        ProductDetailResponse response = customerProductMapper.toProductDetailResponse(product);

        if (product.getBrandId() != null) {
            response.setBrandName(brandService.getBrandNameById(product.getBrandId()));
        }
        if (product.getCategoryId() != null) {
            response.setCategoryName(categoryService.getCategoryNameById(product.getCategoryId()));
        }

        return response;
    }
}
