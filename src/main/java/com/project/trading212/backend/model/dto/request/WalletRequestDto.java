package com.project.trading212.backend.model.dto.request;


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
public class WalletRequestDto {
    @NotNull
    private String walletName;
    @NotNull
    @DecimalMin("0.000001")
    private BigDecimal walletBalance;
}
