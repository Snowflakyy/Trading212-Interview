package com.project.trading212.backend.model;

import com.project.trading212.backend.model.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type",nullable = false)
    private TransactionType transactionType;
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;
    @Column(name = "price_purchased", nullable = false)
    private BigDecimal pricePurchased;
    @Column(name = "created_date",nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;
    @ManyToOne()
    @JoinColumn(name = "crypto_symbol")
    private CryptoWebSocketEntity cryptoWebSocket;
    @ManyToOne
    @JoinColumn(name = "wallet_name")
    private WalletEntity wallet;

}
