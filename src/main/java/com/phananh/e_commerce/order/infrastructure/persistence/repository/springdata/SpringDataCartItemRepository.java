package com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.order.domain.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataCartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long id);

    Optional<CartItem> getByUserIdAndVariantId(Long userId, Long variantId);

    List<CartItem> findByIdIn(List<Long> ids);
}

