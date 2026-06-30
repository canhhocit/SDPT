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
public class ProductUpdateRequest {

    @NotNull(message = "Product id is required")
    private Long productId;

    private String name;

    private String description;

    private Long categoryId;

    private Long brandId;

    private String productAvatarUrl;

    @Valid
    private List<VariantUpdateRequest> variants;

    @Data
    public static class VariantUpdateRequest {
        @NotNull(message = "Variant id is required")
        private Long variantId;

        @NotBlank(message = "SKU code is required")
        private String skuCode;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price must be >= 0")
        private BigDecimal price;

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity must be >= 0")
        private Integer stockQuantity;

        /**
         * URL of the image already uploaded to Cloudinary. If null -> keep existing image.
         * If empty string ("") -> remove existing image.
         */
        private String variantAvatarUrl;

        private Map<String, String> attributes;

        private List<Long> variantImageIdsToDelete;

        private List<String> variantImagesUrlsToAdd;
    }
}


