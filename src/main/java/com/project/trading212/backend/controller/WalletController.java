package com.project.trading212.backend.controller;

import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;
import com.project.trading212.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor

public class WalletController {
    private final WalletService walletService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<WalletResponseDto> createWallet(@RequestBody WalletRequestDto request) {
        return new ResponseEntity<>(walletService.createWallet(request), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<WalletResponseDto> getWallet(@PathVariable String id) {
        return new ResponseEntity<>(walletService.getWallet(id), HttpStatus.OK);
    }

    @PutMapping(value = "/reset/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WalletResponseDto> resetWallet(@RequestBody WalletRequestDto request, @PathVariable String id) {
        return new ResponseEntity<>(walletService.resetWallet(request, id), HttpStatus.OK);
    }
}
