package com.nashtech.cellphonesfake.repository;

import com.nashtech.cellphonesfake.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = "SELECT t FROM token t INNER JOIN users u ON t.user.id = u.id WHERE u.id = :userId AND (t.expired = FALSE OR t.revoked = FALSE)")
    List<Token> findAllValidTokenByUser(Long userId);

    Optional<Token> findByValue(String token);
}
