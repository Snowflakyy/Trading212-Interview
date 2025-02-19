package com.project.trading212.backend.util.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.trading212.backend.model.CryptoWebSocketEntity;
import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.model.dto.SimpleCryptoWebSocketDto;
import com.project.trading212.backend.repository.CryptoWebSocketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoMapper {
    private final ObjectMapper objectMapper;
    private final CryptoWebSocketRepository cryptoWebSocketRepository;

    public CryptoWebSocketDto cryptoWebSocketEntityToDto(CryptoWebSocketEntity cryptoWebSocketEntity) {
        return CryptoWebSocketDto.builder().name(cryptoWebSocketEntity.getName()).symbol(cryptoWebSocketEntity.getSymbol()).price(cryptoWebSocketEntity.getCurrentPrice()).build();
    }

    public SimpleCryptoWebSocketDto cryptoWebSocketToSimpleDto(CryptoWebSocketEntity cryptoWebSocketEntity) {
        return SimpleCryptoWebSocketDto.builder().name(cryptoWebSocketEntity.getName()).symbol(cryptoWebSocketEntity.getSymbol()).build();
    }

    public String cryptoWebSocketResponseDtoToString(CryptoWebSocketDto cryptoWebSocketDto) {
        try {
            return objectMapper.writeValueAsString(cryptoWebSocketDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting DTO to JSON", e);
        }
    }
}
