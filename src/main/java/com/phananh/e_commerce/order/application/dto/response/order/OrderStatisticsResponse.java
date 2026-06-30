package com.phananh.e_commerce.order.application.dto.response.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderStatisticsResponse {
    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime paymentDate;
    private BigDecimal totalPrice;
    private String status;
    private Boolean isPaid;
}

