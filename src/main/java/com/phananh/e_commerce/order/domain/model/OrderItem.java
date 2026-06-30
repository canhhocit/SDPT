package com.phananh.e_commerce.order.domain.model;

import com.phananh.e_commerce.core.util.NumberUtils;
import com.phananh.e_commerce.order.application.dto.command.OrderItemCreateCommand;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long variantId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    public static OrderItem create(OrderItemCreateCommand command) {
        if(command == null) throw new IllegalArgumentException("Command must not be null");
        if(command.getOrder() == null) throw new IllegalArgumentException("Order must not be null");
        if(NumberUtils.isNullOrNegative(command.getVariantId())) throw new IllegalArgumentException("Variant id must be a positive number");
        if(NumberUtils.isNullOrNegative(command.getQuantity())) throw new IllegalArgumentException("Quantity must be a positive number");
        if(NumberUtils.isNullOrNegative(command.getPrice())) throw new IllegalArgumentException("Price must be a non-negative number");
        return OrderItem.builder()
                .order(command.getOrder())
                .variantId(command.getVariantId())
                .quantity(command.getQuantity())
                .price(command.getPrice())
                .build();
    }
}


