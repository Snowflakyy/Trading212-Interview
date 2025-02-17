package com.project.trading212.backend.service;

import com.project.trading212.backend.model.dto.CryptoWebSocketDto;

import java.util.List;
import java.util.Optional;

public interface CryptoWebSocketService {
//    List<CryptoWebSocketDto> getTop20Prices();

    void handlePriceUpdate(CryptoWebSocketDto cryptoWebSocketDto);

    Optional<CryptoWebSocketDto> getPriceBySymbol(String symbol);
     List<CryptoWebSocketDto> getAllPrices();
    //handles bussiness logic for aggregation

    void initializeCryptoEntries();






//    Optional<BigDecimal> getCurrentPrice(String symbol);
//
//    boolean isPriceAvailable(String symbol);

}
