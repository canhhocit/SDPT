package com.phananh.e_commerce.productcatalog.infrastructure.persistence.specification;

import com.phananh.e_commerce.productcatalog.domain.model.Category;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CategorySearchSpecification {

    public static Specification<Category> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) return criteriaBuilder.conjunction();
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Category> createdAtFrom(LocalDateTime createdDateFrom) {
        return (root, query, criteriaBuilder) -> {
            if (createdDateFrom == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom);
        };
    }

    public static Specification<Category> createdAtTo(LocalDateTime createdDateTo) {
        return (root, query, criteriaBuilder) -> {
            if (createdDateTo == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo);
        };
    }

    public static Specification<Category> modifiedAtFrom(LocalDateTime modifiedDateFrom) {
        return (root, query, criteriaBuilder) -> {
            if (modifiedDateFrom == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("modifiedAt"), modifiedDateFrom);
        };
    }

    public static Specification<Category> modifiedAtTo(LocalDateTime modifiedDateTo) {
        return (root, query, criteriaBuilder) -> {
            if (modifiedDateTo == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.lessThanOrEqualTo(root.get("modifiedAt"), modifiedDateTo);
        };
    }
}

