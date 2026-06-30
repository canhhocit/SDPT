package com.phananh.e_commerce.productcatalog.application.dto.query;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Data
@Builder
public class CategorySearchQuery {
    private String keyword;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
    private LocalDateTime modifiedDateFrom;
    private LocalDateTime modifiedDateTo;
    private Pageable pageable;
}

