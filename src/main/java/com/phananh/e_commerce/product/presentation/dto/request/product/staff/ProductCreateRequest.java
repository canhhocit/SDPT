package com.phananh.e_commerce.product.presentation.dto.request.product.staff;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductCreateRequest {

    private String name;

    private String description;

    private Long categoryId;

    private Long brandId;

    /**
     * URL of the product avatar image already uploaded to Cloudinary.
     * Front-end uploads directly to Cloudinary and sends the resulting URL here.
     */
    private String productAvatarUrl;

    @Valid
    private List<VariantCreateRequest> variants;

    @Data
    public static class VariantCreateRequest {
        @NotBlank(message = "SKU code is required")
        private String skuCode;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be >= 0")
        private BigDecimal price;

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity must be >= 0")
        private Integer stockQuantity;

        private Map<String, String> attributes;

        /**
         * URL of the variant avatar image (primary image) already uploaded to Cloudinary.
         */
        private String variantAvatarUrl;

        /**
         * List of URLs for variant gallery images, already uploaded to Cloudinary.
         */
        private List<String> variantImageUrls;
    }
}


