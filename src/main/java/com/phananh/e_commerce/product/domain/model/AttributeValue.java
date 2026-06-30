package com.phananh.e_commerce.product.domain.model;

import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attribute_values")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", nullable = false)
    private ProductAttribute attribute;

    public static AttributeValue create(String value, ProductAttribute attribute) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Attribute value cannot be null or empty");
        }
        if (attribute == null) {
            throw new IllegalArgumentException("Product attribute cannot be null");
        }

        return AttributeValue.builder()
                .attribute(attribute)
                .value(value.trim())
                .build();
    }
}


