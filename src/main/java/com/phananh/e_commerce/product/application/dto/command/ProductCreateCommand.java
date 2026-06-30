package com.phananh.e_commerce.product.application.dto.command;

import com.phananh.e_commerce.product.domain.model.ProductVariant;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Builder
@Data
public class ProductCreateCommand {
    private String name;

    private String description;

    private String avatarUrl;

    private Long categoryId;

    private Long brandId;

    Set<ProductVariant> variants;
}
