package com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.order.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataOrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_Id(Long orderId);
}


