package com.phananh.e_commerce.order.application.service;

import com.phananh.e_commerce.order.application.dto.response.order.*;
import com.phananh.e_commerce.order.presentation.dto.request.order.CheckoutRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    OrderStatisticsOverviewProjection getStatisticsOverview();
    OrderStatisticsRangeProjection getStatisticsByDateRange(LocalDateTime fromDate, LocalDateTime toDate);
    OrderRevenueSummaryProjection getRevenueSummary(LocalDateTime fromDate, LocalDateTime toDate);
    List<OrderRevenuePeriodProjection> getRevenueReport(LocalDateTime fromDate, LocalDateTime toDate, String groupBy);
    Page<StaffOrderResponse> getAllOrders(Pageable pageable);

    OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest);

    void checkout(CheckoutRequest checkoutRequest);

    List<OrderSummaryResponse> getMyOrder();

    OrderDetailResponse getOrderDetail(Long orderId);

    void updateOrderStatus(Long orderId, String status);
}


