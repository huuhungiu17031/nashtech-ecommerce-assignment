package com.nashtech.cellphonesfake.repository;


import com.nashtech.cellphonesfake.model.ProductGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductGalleryRepository extends JpaRepository<ProductGallery, Long> {
    List<ProductGallery> findByProduct_Id(Long productId);
    Optional<ProductGallery> findByProductIdAndThumbnailTrue(Long productId);
    Optional<ProductGallery> findByProduct_IdAndThumbnailIsTrue(Long productId);
}
