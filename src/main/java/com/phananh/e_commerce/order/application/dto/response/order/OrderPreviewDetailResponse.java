package com.phananh.e_commerce.order.application.dto.response.order;

import com.phananh.e_commerce.order.domain.model.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderPreviewDetailResponse {
    private String fullName;
    private List<PaymentMethod> paymentMethods;
    private String phoneNumber;
    private String shippingAddress;
    private BigDecimal shippingFee;
    private BigDecimal totalPrice;
    private List<Item> items;

    @Data
    @Builder
    public static class Item {
        private Long productId;
        private String productName;
        private String skuCode;
        private Integer quantity;
        private BigDecimal price;
        private String variantImageUrl;
    }
}


