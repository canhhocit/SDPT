package com.phananh.e_commerce.productcatalog.domain.model;

import com.phananh.e_commerce.core.domain.model.entity.BaseEntity;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isEnabled;

    public static Category create(String name, String description, String imageUrl) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Category name cannot be null or blank");
        }
        if (description != null) {
            if (description.isBlank()) {
                throw new IllegalArgumentException("Category description cannot be blank");
            }
            description = description.trim();
        }
        if (imageUrl != null) {
            if (imageUrl.isBlank()) {
                throw new IllegalArgumentException("Category image url cannot be blank");
            }
            imageUrl = imageUrl.trim();
        }
        return Category.builder()
                .name(name.trim())
                .description(description)
                .imageUrl(imageUrl)
                .isEnabled(true)
                .build();
    }

    public String buildCategoryAvatarPublicId() {
        return "category-" + id + "-avatar";
    }

    public void updateImage(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            throw new IllegalArgumentException("Category image url cannot be null or blank");
        }
        this.imageUrl = imageUrl.trim();
    }

    public void removeImage() {
        this.imageUrl = null;
    }

    public void updateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Category name cannot be null or blank");
        }
        this.name = name.trim();
    }

    public void updateDescription(String description) {
        this.description = StringUtils.isBlank(description) ? null : description.trim();
    }

    public void active() {
        this.isEnabled = true;
    }

    public void inactive() {
        this.isEnabled = false;
    }
}


