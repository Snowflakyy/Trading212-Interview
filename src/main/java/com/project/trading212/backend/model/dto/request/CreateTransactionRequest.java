package com.project.trading212.backend.model.dto.request;

import com.project.trading212.backend.model.enumeration.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateTransactionRequest {

    private String walletName;
    private String cryptoSymbol;
    @NotNull
    @DecimalMin("0.000001")
    private BigDecimal quantity;
    private BigDecimal pricePurchased;
    private TransactionType transactionType;
}
