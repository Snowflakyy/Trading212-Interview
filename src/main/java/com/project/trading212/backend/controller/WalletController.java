package com.project.trading212.backend.controller;

import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;
import com.project.trading212.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final WalletService walletService;

    @PostMapping()
    public ResponseEntity<WalletResponseDto> createWallet(@RequestBody WalletRequestDto request){
        log.debug("Received request to create a transaction with name: {} and balance:{}",request.getWalletName(),request.getWalletBalance());
        return new ResponseEntity<>(walletService.createWallet(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDto> getWallet(@PathVariable String id){
        return new ResponseEntity<>(walletService.getWallet(id),HttpStatus.OK);
    }

    @DeleteMapping("/reset/{id}")
    public ResponseEntity<WalletResponseDto> resetWallet(@RequestBody WalletRequestDto request,@PathVariable String id){
        return new ResponseEntity<>(walletService.resetWallet(request,id),HttpStatus.OK);
    }
}
