package com.project.trading212.backend.repository;

import com.project.trading212.backend.model.CryptoWebSocketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoWebSocketRepository extends JpaRepository<CryptoWebSocketEntity, Long> {
    Optional<CryptoWebSocketEntity> findBySymbol(String symbol);
}
