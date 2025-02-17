package com.project.trading212.backend.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SimpleCryptoWebSocketDto {
    private String name;
    private String symbol;
}
