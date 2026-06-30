package com.phananh.e_commerce.productcatalog.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BrandResponse {
    private Long brandId;
    private String brandName;
    private String brandDescription;
    private String brandImage;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
}


