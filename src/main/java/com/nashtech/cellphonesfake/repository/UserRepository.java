package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
