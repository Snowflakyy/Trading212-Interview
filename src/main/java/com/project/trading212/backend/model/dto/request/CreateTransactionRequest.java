package com.project.trading212.backend.model.dto.request;

import com.project.trading212.backend.model.enumeration.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateTransactionRequest {
    @NotNull
    private String walletName;
    @NotNull
    private String cryptoSymbol;
    @NotNull
    @DecimalMin("0.000001")
    private BigDecimal quantity;
    @NotNull
    @DecimalMin("0.000001")
    private BigDecimal pricePurchased;
    @NotNull
    private TransactionType transactionType;
}
