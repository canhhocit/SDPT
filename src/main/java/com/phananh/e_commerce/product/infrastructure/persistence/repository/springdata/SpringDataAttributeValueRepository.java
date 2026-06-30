package com.phananh.e_commerce.product.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.product.domain.model.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataAttributeValueRepository extends JpaRepository<AttributeValue, Long> {

}


