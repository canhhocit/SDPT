package com.phananh.e_commerce.productcatalog.presentation.dto.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateRequest {

    @NotNull(message = "Category id is required")
    private Long categoryId;

    private String categoryName;

    private String categoryDescription;

    /**
     * URL of the image already uploaded to Cloudinary. If null -> keep existing image.
     * If empty string ("") -> remove existing image.
     */
    private String imageUrl;
}

