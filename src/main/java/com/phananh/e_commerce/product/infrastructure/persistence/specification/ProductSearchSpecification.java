package com.phananh.e_commerce.product.infrastructure.persistence.specification;

import com.phananh.e_commerce.product.domain.model.Product;
import com.phananh.e_commerce.product.domain.model.ProductVariant;
import org.springframework.data.jpa.domain.Specification;

public class ProductSearchSpecification {

    public static Specification<Product> hasNameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasDescriptionLike(String keyword) {
        return (root, query, criteriaBuilder) ->
                keyword == null || keyword.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Product> hasBrandId(Long brandId) {
        return (root, query, criteriaBuilder) ->
                brandId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return criteriaBuilder.conjunction();

            var subquery = query.subquery(Double.class);
            var variantRoot = subquery.from(ProductVariant.class);

            subquery.select(criteriaBuilder.min(variantRoot.get("price")));
            subquery.where(criteriaBuilder.equal(variantRoot.get("product"), root));

            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(subquery, minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(subquery, minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(subquery, maxPrice);
            }
        };
    }

    public static Specification<Product> hasStatus(String status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }
}


