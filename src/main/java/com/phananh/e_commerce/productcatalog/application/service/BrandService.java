package com.phananh.e_commerce.productcatalog.application.service;

import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.*;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getBrandActive();
    Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request);

    void createBrand(BrandCreateRequest request);

    void updateBrand(BrandUpdateRequest request);

    void updateBrandStatus(Long brandId, String status);

    /**
     * Lấy tên brand từ brand ID
     *
     * @param brandId ID của brand
     * @return Tên brand hoặc null nếu không tìm thấy
     */
    String getBrandNameById(Long brandId);
}


