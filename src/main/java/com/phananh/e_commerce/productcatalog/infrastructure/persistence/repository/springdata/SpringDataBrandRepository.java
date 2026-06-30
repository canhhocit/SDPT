package com.phananh.e_commerce.productcatalog.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataBrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    List<Brand> findByIsEnabledTrue();
    boolean existsByNameIgnoreCase(String name);
}


