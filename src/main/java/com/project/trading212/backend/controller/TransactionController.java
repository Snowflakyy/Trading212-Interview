package com.project.trading212.backend.controller;


import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<CreateTransactionResponse> createTransaction(@RequestBody CreateTransactionRequest request){
        log.debug("Reuqest transaction request: {}", request);
        return new ResponseEntity<>(transactionService.createTransaction(request), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CreateTransactionResponse> getTransactionById(@PathVariable String id){
        return new ResponseEntity<>(transactionService.getTransaction(id),HttpStatus.OK);
    }

    @GetMapping("/holding")
    public ResponseEntity<List<HoldingDto>> getAllHoldings(){
        return new ResponseEntity<>(transactionService.getAllHoldings(),HttpStatus.OK);
    }
}
