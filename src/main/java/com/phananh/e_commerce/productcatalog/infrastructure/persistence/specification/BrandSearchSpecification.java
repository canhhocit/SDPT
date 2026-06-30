package com.phananh.e_commerce.productcatalog.infrastructure.persistence.specification;

import com.phananh.e_commerce.productcatalog.domain.model.Brand;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BrandSearchSpecification {

	public static Specification<Brand> hasKeyword(String keyword) {
		return (root, query, criteriaBuilder) -> {
			if (keyword == null || keyword.isEmpty()) return criteriaBuilder.conjunction();
			String likePattern = "%" + keyword.toLowerCase() + "%";
			return criteriaBuilder.or(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likePattern),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
			);
		};
	}

	public static Specification<Brand> createdAtFrom(LocalDateTime createdDateFrom) {
		return (root, query, criteriaBuilder) -> {
			if (createdDateFrom == null) return criteriaBuilder.conjunction();
			return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdDateFrom);
		};
	}

	public static Specification<Brand> createdAtTo(LocalDateTime createdDateTo) {
		return (root, query, criteriaBuilder) -> {
			if (createdDateTo == null) return criteriaBuilder.conjunction();
			return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdDateTo);
		};
	}

	public static Specification<Brand> modifiedAtFrom(LocalDateTime modifiedDateFrom) {
		return (root, query, criteriaBuilder) -> {
			if (modifiedDateFrom == null) return criteriaBuilder.conjunction();
			return criteriaBuilder.greaterThanOrEqualTo(root.get("modifiedAt"), modifiedDateFrom);
		};
	}

	public static Specification<Brand> modifiedAtTo(LocalDateTime modifiedDateTo) {
		return (root, query, criteriaBuilder) -> {
			if (modifiedDateTo == null) return criteriaBuilder.conjunction();
			return criteriaBuilder.lessThanOrEqualTo(root.get("modifiedAt"), modifiedDateTo);
		};
	}
}
