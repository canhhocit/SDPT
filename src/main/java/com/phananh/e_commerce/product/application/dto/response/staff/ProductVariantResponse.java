package com.phananh.e_commerce.product.application.dto.response.staff;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProductVariantResponse {
    private Long id;
    private String skuCode;
    private Double price;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private Set<Image> variantImageUrl;
    private Set<Attribute> attributes;

    @Data
    public static class Attribute {
        private Long attributeId;
        private String attributeName;
        private String attributeValue;
    }

    @Data
    public static class Image {
        private Long imageId;
        private String imageUrl;
        private boolean isAvatar;
    }
}
