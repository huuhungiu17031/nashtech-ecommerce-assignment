package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProduct_Id(Long productId);
    List<Rating> findByProduct_IdAndIsPublishedIsTrue(Long productId);
}
