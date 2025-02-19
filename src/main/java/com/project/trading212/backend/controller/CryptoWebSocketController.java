package com.project.trading212.backend.controller;

import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.service.CryptoWebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/price")
@RequiredArgsConstructor
@Slf4j
public class CryptoWebSocketController {
    private final CryptoWebSocketService cryptoPriceService;

    @GetMapping(value = "/top20", produces = "application/json")
    public ResponseEntity<List<CryptoWebSocketDto>> getAllPrices() {
        log.debug("getAllPrices");
        return new ResponseEntity<>(cryptoPriceService.getAllPrices(), HttpStatus.OK);
    }

    @GetMapping(value = "/{symbol}", produces = "application/json")
    public ResponseEntity<CryptoWebSocketDto> getPriceBySymbol(@PathVariable String symbol) {
        return new ResponseEntity<>(cryptoPriceService.getPriceBySymbol(symbol), HttpStatus.OK);
    }

}
