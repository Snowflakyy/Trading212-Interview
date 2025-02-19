package com.project.trading212.backend.repository;

import com.project.trading212.backend.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    Optional<TransactionEntity> findByWallet_WalletNameAndCryptoWebSocket_Symbol(String walletName, String cryptoSymbol);

    int deleteAllByWallet_WalletName(String walletName);
}
