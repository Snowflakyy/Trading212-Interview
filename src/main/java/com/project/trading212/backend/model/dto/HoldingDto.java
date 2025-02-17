package com.project.trading212.backend.model.dto;


import com.project.trading212.backend.exception.InsufficientQuantityException;
import lombok.*;

import java.math.BigDecimal;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION;

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

    public void deductQantity(BigDecimal amount){
        if(this.holdingQuantity.compareTo(amount)<0){
            throw new InsufficientQuantityException(INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION);
        }
        this.holdingQuantity = this.holdingQuantity.subtract(amount);
    }
}
