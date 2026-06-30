package com.phananh.e_commerce.productcatalog.presentation.dto.request.brand;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandCreateRequest {

    @NotBlank(message = "Brand name is required")
    private String name;

    private String description;

    /**
     * URL of the image already uploaded to Cloudinary (or another CDN).
     * The front-end should upload the file directly to Cloudinary and send
     * the resulting URL here. No MultipartFile is expected anymore.
     */
    private String imageUrl;
}


