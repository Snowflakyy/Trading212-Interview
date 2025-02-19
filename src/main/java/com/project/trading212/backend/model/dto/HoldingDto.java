package com.project.trading212.backend.model.dto;


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
public class HoldingDto {
    private String cryptoName;
    private String cryptoSymbol;
    private BigDecimal holdingQuantity;
    private BigDecimal holdingReturn;
    private BigDecimal profitLoss;
    private BigDecimal profitLossPercentage;
    private boolean isProfit;
}
