package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCreatedBy(String email);
}
