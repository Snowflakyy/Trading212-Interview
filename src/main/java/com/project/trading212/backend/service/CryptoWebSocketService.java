package com.project.trading212.backend.service;

import com.project.trading212.backend.model.dto.CryptoWebSocketDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CryptoWebSocketService {

    void handlePriceUpdate(CryptoWebSocketDto cryptoWebSocketDto);

    CryptoWebSocketDto getPriceBySymbol(String symbol);

    List<CryptoWebSocketDto> getAllPrices();

    void initializeCryptoEntries();

    Map<String, BigDecimal> getPrices();
}
