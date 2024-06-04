package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByIsPublishedIsTrue();
}
