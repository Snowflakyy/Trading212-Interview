package com.project.trading212.backend.util.mapper;


import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;
import com.project.trading212.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletMapper {
    private final TransactionService transactionService;

    public WalletEntity walletDtoToEntity(WalletRequestDto request) {

        return WalletEntity.builder().walletName(request.getWalletName()).accountBalance(request.getWalletBalance()).build();
    }

    public WalletResponseDto walletEntityToDto(WalletEntity entity) {
        return WalletResponseDto.builder().walletId(entity.getId()).walletName(entity.getWalletName()).walletBalance(entity.getAccountBalance().add(aggregateReturn())).totalReturn(aggregateReturn()).totalReturnPercentage(calculateTotalReturnPercentage(aggregateReturn())).isProfit(aggregateReturn().compareTo(BigDecimal.ZERO) >= 0).build();

    }

    private BigDecimal aggregateReturn() {
        List<HoldingDto> holdings = transactionService.getAllHoldings();
        return holdings.stream().map(HoldingDto::getHoldingReturn).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalReturnPercentage(BigDecimal totalReturn) {
        if (totalReturn.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalReturn.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
