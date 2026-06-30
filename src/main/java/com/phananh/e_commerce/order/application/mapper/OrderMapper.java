package com.phananh.e_commerce.order.application.mapper;

import com.phananh.e_commerce.order.application.dto.response.order.*;
import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    OrderSummaryResponse toOrderSummaryResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "isPaid", source = "isPaid")
    OrderStatisticsResponse toOrderStatisticsResponse(Order order);

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "paymentMethod", expression = "java(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null)")
    StaffOrderResponse toStaffOrderResponse(Order order);

    default OrderSummaryResponse.Item toOrderSummaryItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return OrderSummaryResponse.Item.builder()
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    default OrderDetailResponse.Item toCustomerOrderDetailItem(OrderItem orderItem, ProductInfoResponse productInfo) {
        return OrderDetailResponse.Item.builder()
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .skuCode(productInfo.getVariantSkuCode())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "isPaid", source = "isPaid")
    @Mapping(target = "paymentMethod", expression = "java(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null)")
    @Mapping(target = "status", expression = "java(order.getStatus() != null ? order.getStatus().toString() : null)")
    @Mapping(target = "items", ignore = true)
    OrderDetailResponse toCustomerOrderDetailResponse(Order order);

    default OrderPreviewDetailResponse.Item toOrderPreviewItem(ProductInfoResponse productInfo) {
        return OrderPreviewDetailResponse.Item.builder()
                .skuCode(productInfo.getVariantSkuCode())
                .productId(productInfo.getProductId())
                .productName(productInfo.getProductName())
                .variantImageUrl(productInfo.getVariantImageUrl())
                .build();
    }

    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "shippingAddress", source = "address")
    OrderPreviewDetailResponse toOrderPreviewDetailResponse(UserInfoResponse userInfo);
}


