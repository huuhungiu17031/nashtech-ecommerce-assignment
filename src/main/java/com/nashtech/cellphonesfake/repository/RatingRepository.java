package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findByProduct_Id(Long productId, Pageable pageable);
    Page<Rating> findByProduct_IdAndIsPublishIsTrue(Long productId, Pageable pageable);
    @Query("SELECT AVG(r.score) from rating r WHERE r.product.id = :productId")
    Double getAverageRating(Long productId);
}
