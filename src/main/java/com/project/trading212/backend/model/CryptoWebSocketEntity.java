package com.project.trading212.backend.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "crypto_websocket")
public class CryptoWebSocketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "symbol", unique = true)
    private String symbol;
    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    @OneToMany(mappedBy = "cryptoWebSocket", cascade = CascadeType.REMOVE)
    private List<TransactionEntity> transaction;

}
