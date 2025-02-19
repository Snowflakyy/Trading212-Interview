package com.project.trading212.backend.service.impl;

import com.project.trading212.backend.exception.InsufficientFundsException;
import com.project.trading212.backend.exception.NoSuchTransactionException;
import com.project.trading212.backend.exception.NoSuchWalletException;
import com.project.trading212.backend.model.TransactionEntity;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.TransactionDto;
import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.model.enumeration.TransactionType;
import com.project.trading212.backend.repository.TransactionRepository;
import com.project.trading212.backend.repository.WalletRepository;
import com.project.trading212.backend.service.CryptoWebSocketService;
import com.project.trading212.backend.service.TransactionService;
import com.project.trading212.backend.util.mapper.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.INSUFFICIENT_FUNDS_EXCEPTION_DESCRIPTION;
import static com.project.trading212.backend.exception.ErrorCodeAndMessages.INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION;
import static com.project.trading212.backend.exception.ErrorCodeAndMessages.NO_SUCH_TRANSACTION_EXCEPTION_MESSAGE;
import static com.project.trading212.backend.exception.ErrorCodeAndMessages.NO_SUCH_WALLET_EXCEPTION_DESCRIPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;
    private final CryptoWebSocketService cryptoWebSocketService;

    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        String walletName = request.getWalletName();
        String cryptoSymbol = request.getCryptoSymbol();
        BigDecimal quantity = request.getQuantity();
        BigDecimal price = request.getPricePurchased();
        TransactionType type = request.getTransactionType();
        if (type == TransactionType.SELL) {
            List<TransactionDto> existingTransactions = transactionRepository.findByWallet_WalletNameAndCryptoWebSocket_Symbol(walletName, cryptoSymbol).stream().map(transactionMapper::TransactionEntityToDto).toList();

            handleQuantityDeduct(quantity, existingTransactions);

        }
        TransactionEntity transactionEntity = transactionRepository.save(transactionMapper.createTransactionRequestToEntity(request));
        processTransaction(walletName, quantity, price, type);
        return transactionMapper.TransactionEntityToResponseDto(transactionEntity);
    }

    @Override
    public List<CreateTransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream().map(transactionMapper::TransactionEntityToResponseDto).toList();
    }

    @Override
    public CreateTransactionResponse getTransaction(String transactionId) {
        TransactionEntity transactionEntity = transactionRepository.findById(Long.parseLong(transactionId)).orElseThrow(() -> new NoSuchTransactionException(NO_SUCH_TRANSACTION_EXCEPTION_MESSAGE));
        return transactionMapper.TransactionEntityToResponseDto(transactionEntity);
    }

    @Override
    public List<HoldingDto> getAllHoldings() {
        List<TransactionEntity> allTransactionsEntity = transactionRepository.findAll();
        List<TransactionDto> allTransactionDtos = allTransactionsEntity.stream().map(transactionMapper::TransactionEntityToDto).toList();
        return transactionMapper.mapTransactionToHoldingDto(allTransactionDtos,cryptoWebSocketService.getPrices());
    }


    private void processTransaction(String walletName, BigDecimal quantity, BigDecimal price, TransactionType type) {
        BigDecimal totalAmount = quantity.multiply(price);

        WalletEntity walletEntity = walletRepository.findByWalletName(walletName).orElseThrow(() -> new NoSuchWalletException(NO_SUCH_WALLET_EXCEPTION_DESCRIPTION));
        if (type == TransactionType.SELL) {

            credit(walletEntity, totalAmount);
        } else {
            debit(walletEntity, totalAmount);
        }

        walletRepository.save(walletEntity);
    }

    private void handleQuantityDeduct(BigDecimal quantity, List<TransactionDto> transactions) {
        BigDecimal holdingQuantity = transactionMapper.calculateHoldingQuantity(transactions);

        if (holdingQuantity.compareTo(quantity) < 0) {
            throw new InsufficientFundsException(INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION);
        }
    }

    private void debit(WalletEntity walletEntity, BigDecimal totalAmount) {
        if (walletEntity.getAccountBalance().compareTo(totalAmount) < 0) {
            throw new InsufficientFundsException(INSUFFICIENT_FUNDS_EXCEPTION_DESCRIPTION);
        }
        walletEntity.setAccountBalance(walletEntity.getAccountBalance().subtract(totalAmount));

    }

    private void credit(WalletEntity walletEntity, BigDecimal totalAmount) {
        walletEntity.setAccountBalance(walletEntity.getAccountBalance().add(totalAmount));
    }

}
