package com.phananh.e_commerce.productcatalog.presentation.dto.request.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 

@Data
public class BrandUpdateRequest {

    @NotNull(message = "Brand id is required")
    private Long brandId;

    @NotBlank(message = "Brand name is required")
    private String name;

    private String description;

    /**
     * URL of the image already uploaded to Cloudinary. If null -> keep existing image.
     * If empty string ("") -> remove existing image.
     */
    private String imageUrl;
}

