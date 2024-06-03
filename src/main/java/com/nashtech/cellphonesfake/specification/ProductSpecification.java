package com.nashtech.cellphonesfake.specification;

import com.nashtech.cellphonesfake.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    private ProductSpecification() {}
    public static Specification<Product> withDynamicQueryParameters(Long categoryId, Long brandId, Boolean isPublished) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("id"), brandId));
            }
            if (Boolean.TRUE.equals(isPublished)) {
                predicates.add(criteriaBuilder.isTrue(root.get("isPublished")));
            }
            Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicateArray);
        };
    }
}