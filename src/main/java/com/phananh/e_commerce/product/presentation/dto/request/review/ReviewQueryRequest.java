package com.phananh.e_commerce.product.presentation.dto.request.review;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewQueryRequest {

    private Long productId;

    @Builder.Default
    @Min(value = 1, message = "Min rating must be >= 1")
    private Integer minRating = 1;

    @Builder.Default
    @Min(value = 0, message = "Page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 20;
}
