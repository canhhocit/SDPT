package com.phananh.e_commerce.order.application.dto.response.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class StaffOrderResponse {
    private Long orderId;
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private String shippingAddress;
    private BigDecimal shippingFee;
    private BigDecimal totalPrice;
    private String status;
    private Boolean isPaid;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
}

