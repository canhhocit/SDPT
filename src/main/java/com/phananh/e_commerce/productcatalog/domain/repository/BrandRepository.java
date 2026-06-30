package com.phananh.e_commerce.productcatalog.domain.repository;

import com.phananh.e_commerce.productcatalog.application.dto.query.BrandSearchQuery;
import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    List<Brand> getListBrandActive();
    Page<Brand> getListBrandBySearch(BrandSearchQuery query);
    Brand saveAndFlush(Brand brand);
    void save(Brand brand);
    boolean existsByNameIgnoreCase(String normalizedName);
    Optional<Brand> getById(Long brandId);
}
