package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query(value = "SELECT p FROM product p " +
            "WHERE p.category.id = :categoryId AND (:brandId IS NULL OR p.brand.id = :brandId)")
    Page<Product> findByCategoryIdAndIsPublishedIsTrue(Long categoryId, Long brandId, Pageable pageable);

    @Query(value = "SELECT p FROM product p " +
            "WHERE  (:brandId IS NULL OR p.brand.id = :brandId)")
    Page<Product> findProduct(Long brandId, Pageable pageable);

}
