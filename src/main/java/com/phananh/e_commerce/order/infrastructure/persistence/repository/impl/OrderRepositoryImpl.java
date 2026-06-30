package com.phananh.e_commerce.order.infrastructure.persistence.repository.impl;

import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.repository.OrderRepository;
import com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata.SpringDataOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OrderRepositoryImpl implements OrderRepository {

    SpringDataOrderRepository springDataOrderRepository;

    @Override
    public Order save(Order order) {
        return springDataOrderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return springDataOrderRepository.findById(id);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return springDataOrderRepository.findByUserId(userId);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return springDataOrderRepository.findAll(pageable);
    }

    @Override
    public OrderStatisticsOverviewProjection getStatisticsOverview() {
        return springDataOrderRepository.getStatisticsOverview();
    }

    @Override
    public OrderStatisticsRangeProjection getStatisticsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return springDataOrderRepository.getStatisticsByDateRange(fromDate, toDate);
    }

    @Override
    public OrderRevenueSummaryProjection getRevenueSummary(LocalDateTime fromDate, LocalDateTime toDate) {
        return springDataOrderRepository.getRevenueSummary(fromDate, toDate);
    }

    @Override
    public List<OrderRevenuePeriodProjection> getRevenueReport(LocalDateTime fromDate, LocalDateTime toDate, String groupBy) {
        return springDataOrderRepository.getRevenueReport(fromDate, toDate, groupBy);
    }
}


