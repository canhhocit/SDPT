package com.phananh.e_commerce.product.application.dto.response.customer;

import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProductDetailResponse{

    private Long productId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private String avatarUrl;
    private String productName;
    private String productDescription;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private ProductStatus status;
    private Set<ProductVariantDetail> variants;

    @Data
    public static class ProductVariantDetail {
        private Long variantId;
        private String variantSkuCode;
        private BigDecimal variantPrice;
        private Integer stockQuantity;
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
}


