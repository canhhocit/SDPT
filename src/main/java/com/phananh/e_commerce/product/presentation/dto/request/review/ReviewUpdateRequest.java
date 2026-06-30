package com.phananh.e_commerce.product.presentation.dto.request.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {

    @NotNull(message = "Review id is required")
    private Long reviewId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be >= 1")
    @Max(value = 5, message = "Rating must be <= 5")
    private Integer rating;

    private String comment;
}
