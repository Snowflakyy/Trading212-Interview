package com.project.trading212.backend.service;

import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;

import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.model.dto.HoldingDto;

import java.util.List;

public interface TransactionService {
    CreateTransactionResponse createTransaction(CreateTransactionRequest request);
    CreateTransactionResponse getTransaction(String transactionId);

    List<HoldingDto> getAllHoldings();

}
