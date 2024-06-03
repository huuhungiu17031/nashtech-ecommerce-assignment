package com.nashtech.cellphonesfake.specification;

import com.nashtech.cellphonesfake.model.Product;
import com.nashtech.cellphonesfake.model.User;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    private UserSpecification() {}

    public static Specification<User> withDynamicQueryParameters() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
            return criteriaBuilder.and(predicateArray);
        };
    }
}