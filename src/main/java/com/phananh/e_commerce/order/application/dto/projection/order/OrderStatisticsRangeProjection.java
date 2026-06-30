package com.phananh.e_commerce.order.application.dto.projection.order;

import java.math.BigDecimal;

public interface OrderStatisticsRangeProjection {
    Long getTotalOrders();
    Long getPaidOrders();
    Long getPendingOrders();
    Long getConfirmedOrders();
    Long getShippingOrders();
    Long getDeliveredOrders();
    Long getCancelledOrders();
    Long getReturnedOrders();
    BigDecimal getTotalRevenue();
}

