package com.phananh.e_commerce.order.application.dto.projection.order;

import java.math.BigDecimal;

public interface OrderRevenuePeriodProjection {
    String getPeriod();
    Long getOrders();
    Long getPaidOrders();
    BigDecimal getRevenue();
}

