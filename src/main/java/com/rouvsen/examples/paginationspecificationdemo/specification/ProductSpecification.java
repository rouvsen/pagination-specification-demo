package com.rouvsen.examples.paginationspecificationdemo.specification;

import com.rouvsen.examples.paginationspecificationdemo.dao.entity.ProductEntity;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<ProductEntity> hasName(String name) {
        return (root, query, cb) -> {
            if (Objects.isNull(name) || name.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get(ProductEntity.Fields.name)), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<ProductEntity> hasCategory(String category) {
        return (root, query, cb) -> {
            if (Objects.isNull(category) || category.isBlank()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get(ProductEntity.Fields.category)), category.toLowerCase());
        };
    }

    public static Specification<ProductEntity> priceGreaterThanOrEqual(Double minPrice) {
        return (root, query, cb) -> {
            if (Objects.isNull(minPrice)) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get(ProductEntity.Fields.price), minPrice);
        };
    }

    public static Specification<ProductEntity> priceLessThanOrEqual(Double maxPrice) {
        return (root, query, cb) -> {
            if (Objects.isNull(maxPrice)) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get(ProductEntity.Fields.price), maxPrice);
        };
    }
}

