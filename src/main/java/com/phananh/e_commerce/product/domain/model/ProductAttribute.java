package com.phananh.e_commerce.product.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_attributes")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "attribute", fetch = FetchType.LAZY)
    private Set<AttributeValue> attributeValues = new HashSet<>();
}


