package com.phananh.e_commerce.order.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long variantId;

    @Column(nullable = false)
    private Integer quantity;

    public static CartItem addItem(Long userId, Long variantId, Integer quantity) {
        return CartItem.builder()
                .userId(userId)
                .variantId(variantId)
                .quantity(quantity)
                .build();
    }

    public void updateVariant(Long variantId) {
        if (variantId == null) throw new IllegalArgumentException("Variant ID cannot be null");
        this.variantId = variantId;
    }

    public void updateQuantity(Integer quantity) {
        if (quantity == null) throw new IllegalArgumentException("Quantity cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        this.quantity = quantity;
    }
}


