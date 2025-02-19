package com.project.trading212.backend.service;


import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;

import java.math.BigDecimal;
import java.util.Map;

public interface WalletService {
    WalletResponseDto createWallet(WalletRequestDto request);

    WalletResponseDto getWallet(String walletId);

    WalletResponseDto resetWallet(WalletRequestDto newWallet, String walletId);


}
