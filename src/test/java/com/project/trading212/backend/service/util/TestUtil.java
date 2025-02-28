package com.project.trading212.backend.service.util;

import com.project.trading212.backend.model.TransactionEntity;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.TransactionDto;
import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.model.enumeration.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TestUtil {
    // Constants
    public static final String TEST_WALLET_NAME = "TestWallet";
    public static final String BTC_SYMBOL = "BTC";
    public static final BigDecimal DEFAULT_QUANTITY = new BigDecimal("0.5");
    public static final BigDecimal SMALL_QUANTITY = new BigDecimal("0.3");
    public static final BigDecimal BTC_PRICE = new BigDecimal("40000.00");
    public static final BigDecimal WALLET_BALANCE = new BigDecimal("1000.00");
    public static final BigDecimal LOW_BALANCE = new BigDecimal("10.00");
    public static final String TRANSACTION_ID = "1";

    // Build methods for test data
    public static WalletEntity buildValidWalletEntity() {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletName(TEST_WALLET_NAME);
        walletEntity.setAccountBalance(WALLET_BALANCE);
        return walletEntity;
    }

    public static WalletEntity buildLowBalanceWalletEntity() {
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setWalletName(TEST_WALLET_NAME);
        walletEntity.setAccountBalance(LOW_BALANCE);
        return walletEntity;
    }

    public static TransactionEntity buildValidTransactionEntity() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(Long.parseLong(TRANSACTION_ID));
        transactionEntity.setQuantity(DEFAULT_QUANTITY);
        transactionEntity.setPricePurchased(BTC_PRICE);
        transactionEntity.setCreatedDate(LocalDateTime.now());
        transactionEntity.setTransactionType(TransactionType.BUY);
        return transactionEntity;
    }

    public static TransactionDto buildValidTransactionDto() {
        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setQuantity(DEFAULT_QUANTITY);
        transactionDto.setPricePurchased(BTC_PRICE);
        transactionDto.setTransactionType(TransactionType.BUY);

        return transactionDto;
    }

    public static CreateTransactionRequest buildValidBuyTransactionRequest() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletName(TEST_WALLET_NAME);
        request.setCryptoSymbol(BTC_SYMBOL);
        request.setQuantity(DEFAULT_QUANTITY);
        request.setPricePurchased(BTC_PRICE);
        request.setTransactionType(TransactionType.BUY);
        return request;
    }

    public static CreateTransactionRequest buildValidSellTransactionRequest() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletName(TEST_WALLET_NAME);
        request.setCryptoSymbol(BTC_SYMBOL);
        request.setQuantity(SMALL_QUANTITY);
        request.setPricePurchased(BTC_PRICE);
        request.setTransactionType(TransactionType.SELL);
        return request;
    }

    public static CreateTransactionResponse buildValidTransactionResponse() {
        CreateTransactionResponse response = new CreateTransactionResponse();
        response.setWalletName(TEST_WALLET_NAME);
        response.setQuantity(DEFAULT_QUANTITY);
        response.setPricePurchased(BTC_PRICE);
        response.setTransactionType(TransactionType.BUY);
        return response;
    }

    public static HoldingDto buildValidHoldingDto() {
        HoldingDto holdingDto = new HoldingDto();
        holdingDto.setCryptoSymbol(BTC_SYMBOL);
        holdingDto.setHoldingQuantity(DEFAULT_QUANTITY);
        holdingDto.setHoldingReturn(new BigDecimal("42000.00"));
        return holdingDto;
    }

    public static Map<String, BigDecimal> buildValidCryptoPrices() {
        Map<String, BigDecimal> prices = new HashMap<>();
        prices.put(BTC_SYMBOL, new BigDecimal("42000.00"));
        return prices;
    }
}

