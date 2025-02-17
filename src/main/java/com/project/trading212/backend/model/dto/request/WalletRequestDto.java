package com.project.trading212.backend.model.dto.request;


import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WalletRequestDto {
        private String walletName;
                private BigDecimal walletBalance;
}
