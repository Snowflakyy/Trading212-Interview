package com.project.trading212.backend.model.dto.response;

import com.project.trading212.backend.model.dto.SimpleCryptoWebSocketDto;
import com.project.trading212.backend.model.enumeration.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateTransactionResponse {
    private TransactionType transactionType;
    private BigDecimal quantity;
    private BigDecimal pricePurchased;
    private SimpleCryptoWebSocketDto crypto;
    private String createdDate;
    private String walletName;
}
