package com.phananh.e_commerce.order.application.dto.projection.order;

import java.math.BigDecimal;

public interface OrderStatisticsOverviewProjection {
    Long getTotalOrders();
    Long getPaidOrders();
    Long getPendingOrders();
    BigDecimal getTotalRevenue();
}

