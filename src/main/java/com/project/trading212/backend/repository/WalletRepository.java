package com.project.trading212.backend.repository;

import com.project.trading212.backend.model.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity,Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<WalletEntity> findById(Long id);
    Optional<WalletEntity> findByWalletName(String name);
    void removeWalletEntityById(Long id);
}
