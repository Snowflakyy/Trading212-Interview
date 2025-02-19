package com.project.trading212.backend.service.impl;


import com.project.trading212.backend.exception.NoSuchCryptoException;
import com.project.trading212.backend.model.CryptoWebSocketEntity;
import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.repository.CryptoWebSocketRepository;
import com.project.trading212.backend.service.CryptoWebSocketService;
import com.project.trading212.backend.util.CryptoInitializerClass;
import com.project.trading212.backend.util.mapper.CryptoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.NO_SUCH_CRYPTO_EXCEPTION_DESCRIPTION;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoWebSocketServiceImpl implements CryptoWebSocketService {
    private final CryptoWebSocketRepository cryptoRepository;
    private final CryptoMapper cryptoMapper;
    private final Map<String, BigDecimal> currentPrices = new ConcurrentHashMap<>();

    @Override
    public void handlePriceUpdate(CryptoWebSocketDto cryptoWebSocketDto) {
        currentPrices.put(cryptoWebSocketDto.getSymbol(), cryptoWebSocketDto.getPrice());
        CryptoWebSocketEntity entity = cryptoRepository.findBySymbol(cryptoWebSocketDto.getSymbol()).orElse(new CryptoWebSocketEntity());
        entity.setSymbol(cryptoWebSocketDto.getSymbol());
        entity.setName(cryptoWebSocketDto.getName());
        entity.setCurrentPrice(cryptoWebSocketDto.getPrice());


        cryptoRepository.save(entity);
    }

    @Override
    public CryptoWebSocketDto getPriceBySymbol(String symbol) {
         CryptoWebSocketEntity cryptoWebSocketEntity =cryptoRepository.findBySymbol(symbol).orElseThrow(() -> new NoSuchCryptoException(NO_SUCH_CRYPTO_EXCEPTION_DESCRIPTION));
         return cryptoMapper.cryptoWebSocketEntityToDto(cryptoWebSocketEntity);
    }

    @Override
    public List<CryptoWebSocketDto> getAllPrices() {
       return cryptoRepository.findAll().stream().map(cryptoMapper::cryptoWebSocketEntityToDto).sorted(Comparator.comparing(CryptoWebSocketDto::getPrice).reversed()).toList();
    }

    @Override
    public void initializeCryptoEntries() {
        CryptoInitializerClass.getAllSymbols().forEach(symbol -> {
            String name = CryptoInitializerClass.getFullName(symbol);
            CryptoWebSocketEntity crypto = cryptoRepository.findBySymbol(symbol).orElse(new CryptoWebSocketEntity());

            crypto.setSymbol(symbol);
            crypto.setName(name);
            crypto.setCurrentPrice(BigDecimal.ZERO);
            crypto.setLastUpdated(LocalDateTime.now());
            cryptoRepository.save(crypto);


            log.info("Initialized crypto entry for {} ({})", name, symbol);
        });

    }

    @Override
    public Map<String, BigDecimal> getPrices() {
        return currentPrices;
    }


}
