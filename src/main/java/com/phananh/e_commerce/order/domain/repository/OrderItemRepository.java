package com.phananh.e_commerce.order.domain.repository;

import com.phananh.e_commerce.order.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {
    OrderItem save(OrderItem orderItem);
    List<OrderItem> findByOrder_Id(Long orderId);
}


