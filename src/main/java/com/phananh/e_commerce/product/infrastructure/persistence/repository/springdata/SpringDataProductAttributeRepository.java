package com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.product.domain.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataProductAttributeRepository extends JpaRepository<ProductAttribute, Long> {
    Optional<ProductAttribute> findByName(String name);
}


