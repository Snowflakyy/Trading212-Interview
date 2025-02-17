package com.project.trading212.backend.service.impl;


import com.project.trading212.backend.model.CryptoWebSocketEntity;
import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.repository.CryptoWebSocketRepository;
import com.project.trading212.backend.service.CryptoWebSocketService;
import com.project.trading212.backend.util.CryptoMapping;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoWebSocketServiceImpl implements CryptoWebSocketService {
    private final CryptoWebSocketRepository cryptoRepository;
    private final ConcurrentHashMap<String,CryptoWebSocketDto> priceCache = new ConcurrentHashMap<>();




    @Override
    public void handlePriceUpdate(CryptoWebSocketDto cryptoWebSocketDto) {
        priceCache.put(cryptoWebSocketDto.getSymbol(),cryptoWebSocketDto);

        CryptoWebSocketEntity entity = cryptoRepository.findBySymbol(cryptoWebSocketDto.getSymbol())
                .orElse(new CryptoWebSocketEntity());

        entity.setSymbol(cryptoWebSocketDto.getSymbol());
        entity.setName(cryptoWebSocketDto.getName());
        entity.setCurrentPrice(cryptoWebSocketDto.getPrice());
        entity.setLastUpdated(cryptoWebSocketDto.getLastUpdated());

        cryptoRepository.save(entity);
    }

    @Override
    public Optional<CryptoWebSocketDto> getPriceBySymbol(String symbol) {
        return Optional.ofNullable(priceCache.get(symbol));
    }
    @Override
    public List<CryptoWebSocketDto> getAllPrices() {
        return new ArrayList<>(priceCache.values()).stream().sorted(Comparator.comparing(CryptoWebSocketDto::getPrice).reversed()).toList();
    }

    @Override
    public void initializeCryptoEntries() {
        CryptoMapping.getAllSymbols().forEach(symbol -> {
            String name = CryptoMapping.getFullName(symbol);
            CryptoWebSocketEntity crypto = cryptoRepository.findBySymbol(symbol).orElse(new CryptoWebSocketEntity());

            crypto.setSymbol(symbol);
            crypto.setName(name);
            crypto.setCurrentPrice(BigDecimal.ZERO);
            crypto.setLastUpdated(LocalDateTime.now());
            cryptoRepository.save(crypto);
        priceCache.put(symbol,new CryptoWebSocketDto(name,symbol,BigDecimal.ZERO,LocalDateTime.now()));

            log.info("Initialized crypto entry for {} ({})",name,symbol);
        });

    }


}
