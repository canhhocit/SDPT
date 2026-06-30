package com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SpringDataProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByNameContainingIgnoreCase(String name);
    Product findByVariants_Id(Long variantId);
}


