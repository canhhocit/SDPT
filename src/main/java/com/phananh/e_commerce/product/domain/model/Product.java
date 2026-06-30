package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.command.ProductCreateCommand;
import com.phananh.e_commerce.product.domain.model.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "brand_id")
    private Long brandId;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Set<ProductVariant> variants = new HashSet<>();

    public static Product create(ProductCreateCommand command) {

        return Product.builder()
                .name(StringUtils.isBlank(command.getName()) ? "Empty" : command.getName().trim())
                .description(StringUtils.isBlank(command.getDescription()) ? null : command.getDescription().trim())
                .avatarUrl(StringUtils.isBlank(command.getAvatarUrl()) ? null : command.getAvatarUrl().trim())
                .status(ProductStatus.DRAFT)
                .categoryId(command.getCategoryId())
                .brandId(command.getBrandId())
                .variants(command.getVariants())
                .build();
    }

    public void updateName(String name) {
        this.name = StringUtils.isBlank(name) ? "Empty" : name.trim();
    }

    public void updateDescription(String description) {
        this.description = StringUtils.isBlank(description) ? null : description.trim();
    }

    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = StringUtils.isBlank(avatarUrl) ? null : avatarUrl.trim();
    }

    public void inactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }

    public void isDraft() {
        this.status = ProductStatus.DRAFT;
    }

    public void updateCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void updateBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public void addVariant(ProductVariant variant) {
        this.variants.add(variant);
    }
}


