package com.phananh.e_commerce.order.domain.model.enums;

public enum OrderStatus {
    PENDING,     // Chờ xác nhận (Khách vừa bấm đặt hàng)
    CONFIRMED,   // Đã xác nhận (Shop đã kiểm tra kho và chấp nhận đơn)
    SHIPPING,    // Đang giao hàng (Đã giao cho đơn vị vận chuyển)
    DELIVERED,   // Đã giao hàng thành công
    CANCELLED,   // Đã hủy (Bởi khách hoặc bởi Shop)
    RETURNED     // Trả hàng/Hoàn tiền
}


