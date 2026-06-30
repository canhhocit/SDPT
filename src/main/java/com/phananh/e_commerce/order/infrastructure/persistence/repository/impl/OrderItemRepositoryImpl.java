package com.phananh.e_commerce.order.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.order.domain.repository.OrderItemRepository;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata.SpringDataOrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemRepositoryImpl implements OrderItemRepository {

    SpringDataOrderItemRepository springDataOrderItemRepository;

    @Override
    public OrderItem save(OrderItem orderItem) {
        return springDataOrderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> findByOrder_Id(Long orderId) {
        return springDataOrderItemRepository.findByOrder_Id(orderId);
    }
}


