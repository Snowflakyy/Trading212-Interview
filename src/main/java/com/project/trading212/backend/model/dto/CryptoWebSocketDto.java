package com.project.trading212.backend.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CryptoWebSocketDto {
    private String name;
    private String symbol;
    private BigDecimal price;
    private LocalDateTime lastUpdated;
}

