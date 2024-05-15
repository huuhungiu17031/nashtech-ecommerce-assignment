package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.CartDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends CrudRepository<CartDetail, Long> {
}
