package com.project.trading212.backend.service;

import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;

import java.util.List;

public interface TransactionService {
    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    List<CreateTransactionResponse> getAllTransactions();

    CreateTransactionResponse getTransaction(String transactionId);

    List<HoldingDto> getAllHoldings();

}
