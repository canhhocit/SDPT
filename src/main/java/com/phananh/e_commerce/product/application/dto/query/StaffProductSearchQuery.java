package com.phananh.e_commerce.product.application.dto.query;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@Builder
public class StaffProductSearchQuery {
    private String keyword;
    private Long categoryId;
    private Long brandId;
    private Double minPrice;
    private Double maxPrice;
    private Integer minRating;
    private Pageable pageable;
}

