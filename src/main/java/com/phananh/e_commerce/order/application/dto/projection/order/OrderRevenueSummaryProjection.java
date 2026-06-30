package com.phananh.e_commerce.order.application.dto.projection.order;

import java.math.BigDecimal;

public interface OrderRevenueSummaryProjection {
    Long getTotalOrders();
    Long getPaidOrders();
    BigDecimal getTotalRevenue();
}

