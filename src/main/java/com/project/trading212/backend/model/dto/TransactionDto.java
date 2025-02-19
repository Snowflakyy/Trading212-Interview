package com.project.trading212.backend.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.project.trading212.backend.model.enumeration.TransactionType;
import com.project.trading212.backend.util.serializer.CustomDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionDto {
    private TransactionType transactionType;
    private BigDecimal quantity;
    private BigDecimal pricePurchased;
    private CryptoWebSocketDto crypto;

    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime createdDate;
    private String walletName;
}
