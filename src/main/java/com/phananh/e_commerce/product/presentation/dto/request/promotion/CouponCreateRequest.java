package com.phananh.e_commerce.product.presentation.dto.request.promotion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class CouponCreateRequest {

    @NotBlank(message = "Coupon code is required")
    private String code;

    private String description;

    @NotBlank(message = "Discount type is required")
    private String discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be > 0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Min order value must be >= 0")
    private BigDecimal minOrderValue;

    @DecimalMin(value = "0.0", message = "Max discount value must be >= 0")
    private BigDecimal maxDiscountValue;

    @NotNull(message = "Start time is required")
    private LocalDateTime startAt;

    @NotNull(message = "End time is required")
    private LocalDateTime endAt;

    @NotNull(message = "Total usage limit is required")
    @Min(value = 1, message = "Total usage limit must be >= 1")
    private Integer totalUsageLimit;

    @Builder.Default
    @Min(value = 1, message = "Usage per user limit must be >= 1")
    private Integer usagePerUserLimit = 1;

    @Builder.Default
    private Boolean enabled = true;
}
