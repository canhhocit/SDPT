package com.phananh.e_commerce.order.application.dto.command;

import com.phananh.e_commerce.order.domain.model.Order;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemCreateCommand {
    private Order order;
    private Long variantId;
    private Integer quantity;
    private BigDecimal price;
}
