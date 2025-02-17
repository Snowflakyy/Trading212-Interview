package com.project.trading212.backend.model.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CryptoWebSocketResponseDto {
    private String name;
    private String symbol;
    private BigDecimal price;
    private String lastUpdated;
}
