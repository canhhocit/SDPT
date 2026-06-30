package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import com.phananh.e_commerce.core.util.ListUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import com.phananh.e_commerce.product.application.dto.command.ProductVariantCreateCommand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_variants")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "sku_code", unique = true, nullable = false)
    private String skuCode;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Version
    private Long version;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "variant_attribute_values",
            joinColumns = @JoinColumn(name = "variant_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id", nullable = false)
    )
    private Set<AttributeValue> attributeValues = new HashSet<>();

    @OneToMany(mappedBy = "variant", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<VariantImage> images = new HashSet<>();

    public static ProductVariant create(ProductVariantCreateCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }
        if (command.getProduct() == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        return ProductVariant.builder()
                .product(command.getProduct())
                .skuCode(StringUtils.isBlank(command.getSkuCode()) ? "empty" : command.getSkuCode().trim())
                .price(command.getPrice().signum() < 0 ? BigDecimal.ZERO : command.getPrice())
                .stockQuantity(command.getStockQuantity() == null || command.getStockQuantity() < 0 ? 0 : command.getStockQuantity())
                .images(command.getImages() == null ? new HashSet<>() : command.getImages())
                .attributeValues(command.getAttributeValues() == null ? new HashSet<>() : command.getAttributeValues())
                .build();
    }

    public void updateSkuCode(String skuCode) {
        this.skuCode = StringUtils.isBlank(skuCode) ? "empty" : skuCode.trim();
    }

    public void updatePrice(BigDecimal price) {
        if(price == null) throw new IllegalArgumentException("Price is null");
        if(price.signum() < 0) throw new IllegalArgumentException("Price >= 0");
        this.price = price;
    }

    public void updateStockQuantity(Integer stockQuantity) {
        if(stockQuantity == null) throw new IllegalArgumentException("Stock quantity is null");
        if(stockQuantity < 0) throw new IllegalArgumentException("Stock quantity >= 0");
        this.stockQuantity = stockQuantity;
    }

    public void updateAttributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues.clear();
        this.attributeValues = attributeValues == null ? new HashSet<>() : attributeValues;
    }

    public void removeAvatar(){
        this.images.removeIf(VariantImage::isAvatar);
    }

    public void updateAvatar(VariantImage variantImage) {
        if(variantImage == null) throw new IllegalArgumentException("Image is null");
        this.removeAvatar();
        this.images.add(variantImage);
    }

    public void removeListImages(List<VariantImage> variantImages) {
        if(ListUtils.isNullOrEmpty(variantImages)) throw new IllegalArgumentException("List image is empty");
        for(VariantImage image : variantImages) {
            this.images.remove(image);
        }
    }

    public void addListImage(List<VariantImage> images) {
        if(ListUtils.isNullOrEmpty(images)) throw new IllegalArgumentException("List image is empty");
        this.images.addAll(images);
    }
}


