package com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.product.domain.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProduct_Id(Long productId);
    Optional<ProductVariant> findBySkuCode(String skuCode);
}


