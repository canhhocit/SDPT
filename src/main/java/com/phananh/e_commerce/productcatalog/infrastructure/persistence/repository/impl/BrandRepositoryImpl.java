package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import com.phananh.e_commerce.productcatalog.domain.repository.BrandRepository;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata.SpringDataBrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;

import com.phananh.e_commerce.productcatalog.application.dto.query.BrandSearchQuery;
import com.phananh.e_commerce.productcatalog.infrastructure.persistence.specification.BrandSearchSpecification;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BrandRepositoryImpl implements BrandRepository {

    SpringDataBrandRepository springDataBrandRepository;

    @Override
    public List<Brand> getListBrandActive() {
        return springDataBrandRepository.findByIsEnabledTrue();
    }

    @Override
    public Page<Brand> getListBrandBySearch(BrandSearchQuery query) {
        Pageable pageable = query.getPageable();

        Specification<Brand> specification = Specification
                .where(BrandSearchSpecification.hasKeyword(query.getKeyword()))
                .and(BrandSearchSpecification.createdAtFrom(query.getCreatedDateFrom()))
                .and(BrandSearchSpecification.createdAtTo(query.getCreatedDateTo()))
                .and(BrandSearchSpecification.modifiedAtFrom(query.getModifiedDateFrom()))
                .and(BrandSearchSpecification.modifiedAtTo(query.getModifiedDateTo()));

        return springDataBrandRepository.findAll(specification, pageable);
    }

    @Override
    public Brand saveAndFlush(Brand brand) {
        return springDataBrandRepository.saveAndFlush(brand);
    }

    @Override
    public void save(Brand brand) {
        springDataBrandRepository.save(brand);
    }

    @Override
    public boolean existsByNameIgnoreCase(String normalizedName) {
        return springDataBrandRepository.existsByNameIgnoreCase(normalizedName);
    }

    @Override
    public Optional<Brand> getById(Long brandId) {
        return springDataBrandRepository.findById(brandId);
    }
}
