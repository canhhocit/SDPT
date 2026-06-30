package com.phananh.e_commerce.product.presentation.dto.request.product.staff;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StaffProductSearchRequest {

    private String keyword;

    private Long categoryId;

    private Long brandId;

    @DecimalMin(value = "0.0", message = "Min price must be >= 0")
    private Double minPrice;

    @DecimalMin(value = "0.0", message = "Max price must be >= 0")
    private Double maxPrice;

    @Min(value = 0, message = "Min rating must be >= 0")
    @Max(value = 5, message = "Min rating must be <= 5")
    private Integer minRating;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size;

    private String sortBy;

    private String sortType;
}


