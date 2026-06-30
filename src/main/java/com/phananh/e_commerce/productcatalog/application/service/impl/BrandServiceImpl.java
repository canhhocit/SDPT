package com.phananh.e_commerce.productcatalog.application.service.impl;

import com.phananh.e_commerce.core.util.PageUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.productcatalog.application.dto.query.BrandSearchQuery;
import com.phananh.e_commerce.productcatalog.domain.repository.BrandRepository;
import com.phananh.e_commerce.productcatalog.presentation.dto.request.brand.*;
import com.phananh.e_commerce.productcatalog.application.dto.response.BrandResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.productcatalog.application.mapper.BrandMapper;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.application.service.BrandService;
import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BrandServiceImpl implements BrandService {

    BrandRepository brandRepository;
    BrandMapper brandMapper;
    CloudinaryService cloudinaryService;

    @Override
    public List<BrandResponse> getBrandActive() {
        return brandRepository.getListBrandActive()
                .stream().map(brandMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BrandResponse> getBrandsBySearch(BrandSearchRequest request) {
        int page = PageUtils.getPageNumber(request.getPage());
        int size = PageUtils.getPageSize(request.getSize());
        String sortBy = PageUtils.getSortBy(request.getSortBy());
        String sortType = PageUtils.getSortType(request.getSortType());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortType), sortBy));

        BrandSearchQuery query = BrandSearchQuery.builder()
                .keyword(request.getKeyword() == null ? null : request.getKeyword().trim())
                .createdDateFrom(request.getCreatedDateFrom())
                .createdDateTo(request.getCreatedDateTo())
                .modifiedDateFrom(request.getModifiedDateFrom())
                .modifiedDateTo(request.getModifiedDateTo())
                .pageable(pageable)
                .build();

        return brandRepository.getListBrandBySearch(query)
                .map(brandMapper::toResponse);
    }

    @Override
    @Transactional
    public void createBrand(BrandCreateRequest request) {
        if (brandRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new AppException(ErrorCode.CONFLICT);
        }

        Brand brand = Brand.create(request.getName(), request.getDescription(), request.getImageUrl());

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void updateBrand(BrandUpdateRequest request) {
        Brand brand = brandRepository.getById(request.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        // Update brand info
        brand.updateName(request.getName());
        brand.updateDescription(request.getDescription());

        // Update image handling using imageUrl semantics:
        // - imageUrl == null => keep existing image
        // - imageUrl is non-empty => set this new URL
        // - imageUrl is empty string ("") => remove existing image
        String imageUrl = request.getImageUrl();
        if (imageUrl != null) {
            if (!imageUrl.isBlank()) {
                // Set provided URL directly
                brand.updateImage(imageUrl);
            } else {
                // empty string => remove existing image and delete from Cloudinary
                try {
                    cloudinaryService.deleteFileByUrl(imageUrl);
                } catch (Exception e) {
                    log.error("Error deleting brand image", e);
                    throw new AppException(ErrorCode.FILE_DELETE_ERROR);
                }
                brand.removeImage();
            }
        }

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void updateBrandStatus(Long brandId, String status) {
        Brand brand = brandRepository.getById(brandId)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        if (StringUtils.isBlank(status)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if ("ACTIVE".equalsIgnoreCase(status)) brand.active();
        else if ("INACTIVE".equalsIgnoreCase(status)) brand.inactive();

        brandRepository.save(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public String getBrandNameById(Long brandId) {
        if (brandId == null) {
            return null;
        }
        return brandRepository.getById(brandId)
                .map(Brand::getName)
                .orElse(null);
    }
}


