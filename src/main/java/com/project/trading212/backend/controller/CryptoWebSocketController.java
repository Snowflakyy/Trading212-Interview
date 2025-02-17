package com.project.trading212.backend.controller;

import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.service.CryptoWebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
public class CryptoWebSocketController {
    private final CryptoWebSocketService cryptoPriceService;

//    @GetMapping("/top20")
//    public ResponseEntity<List<CryptoWebSocketDto>> getAllPrices() {
//        return ResponseEntity.ok(cryptoPriceService.getTop20Prices());
//    }
    @GetMapping("/top20")
    public ResponseEntity<List<CryptoWebSocketDto>>getAllPrices(){
        return ResponseEntity.ok(cryptoPriceService.getAllPrices());
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<CryptoWebSocketDto> getPriceBySymbol(@PathVariable String symbol) {
        return cryptoPriceService.getPriceBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
