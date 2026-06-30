package com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.product.domain.model.VariantImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataVariantImageRepository extends JpaRepository<VariantImage, Long> {
    List<VariantImage> findByIdIn(List<Long> ids);
}

