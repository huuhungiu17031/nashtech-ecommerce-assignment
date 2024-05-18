package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    Optional<CartDetail> findByCart_IdAndProduct_Id(Long cartId, Long productId);

    List<CartDetail> findByCart_Id(Long cartId);
}
