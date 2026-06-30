package com.phananh.e_commerce.order.domain.repository;

import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import com.phananh.e_commerce.order.domain.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByUserId(Long userId);
    Page<Order> findAll(Pageable pageable);

    OrderStatisticsOverviewProjection getStatisticsOverview();
    OrderStatisticsRangeProjection getStatisticsByDateRange(LocalDateTime fromDate, LocalDateTime toDate);
    OrderRevenueSummaryProjection getRevenueSummary(LocalDateTime fromDate, LocalDateTime toDate);
    List<OrderRevenuePeriodProjection> getRevenueReport(LocalDateTime fromDate, LocalDateTime toDate, String groupBy);
}


