package com.project.trading212.backend.model.dto.response;

import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WalletResponseDto {
    private Long walletId;
    private String walletName;
    private BigDecimal walletBalance;
    private BigDecimal totalReturn;
    private BigDecimal totalReturnPercentage;
    private boolean isProfit;
}
