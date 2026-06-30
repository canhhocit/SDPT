package com.phananh.e_commerce.order.application.dto.command;

import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.order.domain.model.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class OrderCreateCommand {
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private String shippingAddress;
    private PaymentMethod paymentMethod;
    private Boolean isPaid;
}

