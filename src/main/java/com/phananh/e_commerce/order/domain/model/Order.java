package com.phananh.e_commerce.order.domain.model;

import com.phananh.e_commerce.core.util.NumberUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.order.domain.model.enums.PaymentMethod;
import com.phananh.e_commerce.order.application.dto.command.OrderCreateCommand;
import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "total_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "shipping_fee", precision = 19, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "shipping_address", columnDefinition = "TEXT", nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public static Order createFirst(OrderCreateCommand command) {
        if(command == null) throw new IllegalArgumentException("Command must not be null");
        if(NumberUtils.isNullOrNegative(command.getUserId())) throw new IllegalArgumentException("User id must be a positive number");
        if(StringUtils.isBlank(command.getFullName())) throw new IllegalArgumentException("Full name must not be null or empty");
        if(StringUtils.isBlank(command.getPhoneNumber())) throw new IllegalArgumentException("Phone number must not be null or empty");
        if(StringUtils.isBlank(command.getShippingAddress())) throw new IllegalArgumentException("Shipping address must not be null or empty");
        if(command.getPaymentMethod() == null) throw new IllegalArgumentException("Payment method must not be null");

        Boolean isPaid = command.getIsPaid() != null ? command.getIsPaid() : false;
        return Order.builder()
                .userId(command.getUserId())
                .fullName(command.getFullName())
                .phoneNumber(command.getPhoneNumber())
                .shippingAddress(command.getShippingAddress())
                .paymentMethod(command.getPaymentMethod())
                .isPaid(isPaid)
                .paymentDate(isPaid ? LocalDateTime.now() : null)
                .build();
    }

    public void updateTotalPrice(BigDecimal totalPrice) {
        if(NumberUtils.isNullOrNegative(totalPrice)) throw new IllegalArgumentException("Total price must be a non-negative number");
        this.totalPrice = totalPrice;
    }

    public void updateShippingFee(BigDecimal shippingFee) {
        if(NumberUtils.isNullOrNegative(shippingFee)) throw new IllegalArgumentException("Shipping fee must be a non-negative number");
        this.shippingFee = shippingFee;
    }

    public void updateStatus(OrderStatus status) {
        if (status == null) throw new IllegalArgumentException("Status must not be null");
        this.status = status;
    }

    public void addOrderItems(Set<OrderItem> items) {
        if(items == null || items.isEmpty()) throw new IllegalArgumentException("Order items must not be null or empty");
        orderItems.clear();
        orderItems.addAll(items);
    }
}


