package com.phananh.e_commerce.product.application.dto.response.staff;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String avatarUrl;
    private String name;
    private String description;
    private String status;
    private Long categoryName;
    private Long brandName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
}
