package com.phananh.e_commerce.productcatalog.presentation.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String categoryDescription;

    /**
     * URL of the image already uploaded to Cloudinary (or another CDN).
     * The front-end should upload the file directly to Cloudinary and send
     * the resulting URL here. No MultipartFile is expected anymore.
     */
    private String imageUrl;
}


