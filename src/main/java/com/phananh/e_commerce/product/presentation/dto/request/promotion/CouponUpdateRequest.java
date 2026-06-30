package com.phananh.e_commerce.product.presentation.dto.request.promotion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponUpdateRequest {

    @NotNull(message = "Coupon id is required")
    private Long couponId;

    private String code;
    private String description;
    private String discountType;

    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be > 0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Min order value must be >= 0")
    private BigDecimal minOrderValue;

    @DecimalMin(value = "0.0", message = "Max discount value must be >= 0")
    private BigDecimal maxDiscountValue;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Min(value = 1, message = "Total usage limit must be >= 1")
    private Integer totalUsageLimit;

    @Min(value = 1, message = "Usage per user limit must be >= 1")
    private Integer usagePerUserLimit;

    private Boolean enabled;
}
