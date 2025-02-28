package com.project.trading212.backend.service;

import com.project.trading212.backend.exception.InsufficientFundsException;
import com.project.trading212.backend.exception.NoSuchTransactionException;
import com.project.trading212.backend.exception.NoSuchWalletException;
import com.project.trading212.backend.model.TransactionEntity;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.repository.TransactionRepository;
import com.project.trading212.backend.repository.WalletRepository;
import com.project.trading212.backend.service.impl.TransactionServiceImpl;
import com.project.trading212.backend.util.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.trading212.backend.service.util.TestUtil.buildLowBalanceWalletEntity;
import static com.project.trading212.backend.service.util.TestUtil.buildValidBuyTransactionRequest;
import static com.project.trading212.backend.service.util.TestUtil.buildValidCryptoPrices;
import static com.project.trading212.backend.service.util.TestUtil.buildValidHoldingDto;
import static com.project.trading212.backend.service.util.TestUtil.buildValidSellTransactionRequest;
import static com.project.trading212.backend.service.util.TestUtil.buildValidTransactionDto;
import static com.project.trading212.backend.service.util.TestUtil.buildValidTransactionEntity;
import static com.project.trading212.backend.service.util.TestUtil.buildValidTransactionResponse;
import static com.project.trading212.backend.service.util.TestUtil.buildValidWalletEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class TransactionServiceTest {
    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private WalletRepository walletRepository;
    private TransactionMapper transactionMapper;
    private CryptoWebSocketService cryptoWebSocketService;

    @BeforeEach
    public void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        walletRepository = Mockito.mock(WalletRepository.class);
        transactionMapper = Mockito.mock(TransactionMapper.class);
        cryptoWebSocketService = Mockito.mock(CryptoWebSocketService.class);
        transactionService = new TransactionServiceImpl(transactionRepository, walletRepository, transactionMapper, cryptoWebSocketService);
    }

    @Test
    public void createTransaction_Buy_Success() {
        // Arrange
        CreateTransactionResponse expectedResponse = buildValidTransactionResponse();

        when(walletRepository.findByWalletName(any())).thenReturn(Optional.of(buildValidWalletEntity()));
        when(transactionMapper.createTransactionRequestToEntity(any())).thenReturn(buildValidTransactionEntity());
        when(transactionRepository.save(any())).thenReturn(buildValidTransactionEntity());
        when(transactionMapper.TransactionEntityToResponseDto(any())).thenReturn(buildValidTransactionResponse());

        // Act
        CreateTransactionResponse actualResponse = transactionService.createTransaction(buildValidBuyTransactionRequest());

        // Assert

        assertEquals(actualResponse.getWalletName(), expectedResponse.getWalletName());
        assertEquals(actualResponse.getQuantity(), expectedResponse.getQuantity());
        verify(walletRepository, times(1)).save(any(WalletEntity.class));
    }

    @Test
    public void createTransaction_Buy_InsufficientFunds() {
        // Arrange
        when(walletRepository.findByWalletName(any())).thenReturn(Optional.of(buildLowBalanceWalletEntity()));
        when(transactionMapper.createTransactionRequestToEntity(any())).thenReturn(buildValidTransactionEntity());

        // Act & Assert
        assertThrows(InsufficientFundsException.class, () -> transactionService.createTransaction(buildValidBuyTransactionRequest()));
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    public void createTransaction_Buy_NoSuchWallet() {
        // Arrange
        when(walletRepository.findByWalletName(any())).thenReturn(Optional.empty());
        when(transactionMapper.createTransactionRequestToEntity(any())).thenReturn(buildValidTransactionEntity());

        // Act & Assert
        assertThrows(NoSuchWalletException.class, () -> transactionService.createTransaction(buildValidBuyTransactionRequest()));
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    @Test
    public void createTransaction_Sell_Success() {
        // Arrange
        CreateTransactionResponse expectedResponse = buildValidTransactionResponse();

        when(walletRepository.findByWalletName(any())).thenReturn(Optional.of(buildValidWalletEntity()));
        when(transactionMapper.createTransactionRequestToEntity(any())).thenReturn(buildValidTransactionEntity());
        when(transactionRepository.save(any())).thenReturn(buildValidTransactionEntity());
        when(transactionMapper.TransactionEntityToResponseDto(any())).thenReturn(buildValidTransactionResponse());
        when(transactionRepository.findByWallet_WalletNameAndCryptoWebSocket_Symbol(any(), any())).thenReturn(Optional.of((buildValidTransactionEntity())));
        when(transactionMapper.TransactionEntityToDto(any())).thenReturn(buildValidTransactionDto());
        when(transactionMapper.calculateHoldingQuantity(anyList())).thenReturn(new BigDecimal("0.5"));

        // Act
        CreateTransactionResponse actualResponse = transactionService.createTransaction(buildValidSellTransactionRequest());

        // Assert

        assertEquals(actualResponse.getWalletName(), expectedResponse.getWalletName());
        verify(walletRepository, times(1)).save(any(WalletEntity.class));
    }

    @Test
    public void createTransaction_Sell_InsufficientQuantity() {
        // Arrange
        when(transactionRepository.findByWallet_WalletNameAndCryptoWebSocket_Symbol(any(), any())).thenReturn(Optional.of(buildValidTransactionEntity()));
        when(transactionMapper.TransactionEntityToDto(any())).thenReturn(buildValidTransactionDto());
        when(transactionMapper.calculateHoldingQuantity(anyList())).thenReturn(new BigDecimal("0.2"));
        when(transactionMapper.createTransactionRequestToEntity(any())).thenReturn(buildValidTransactionEntity());

        // Act & Assert
        assertThrows(InsufficientFundsException.class, () -> transactionService.createTransaction(buildValidSellTransactionRequest()));
    }

    @Test
    public void getAllTransactions_Success() {
        // Arrange
        List<CreateTransactionResponse> expectedResponses = List.of(buildValidTransactionResponse());

        when(transactionRepository.findAll()).thenReturn(List.of(buildValidTransactionEntity()));
        when(transactionMapper.TransactionEntityToResponseDto(any())).thenReturn(buildValidTransactionResponse());

        // Act
        List<CreateTransactionResponse> actualResponses = transactionService.getAllTransactions();

        // Assert
        assertEquals(actualResponses.size(), expectedResponses.size());
    }

    @Test
    public void getTransaction_Success() {
        // Arrange
        CreateTransactionResponse expectedResponse = buildValidTransactionResponse();

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(buildValidTransactionEntity()));
        when(transactionMapper.TransactionEntityToResponseDto(any())).thenReturn(buildValidTransactionResponse());

        // Act
        CreateTransactionResponse actualResponse = transactionService.getTransaction("1");

        // Assert

        assertEquals(actualResponse.getWalletName(), expectedResponse.getWalletName());
    }

    @Test
    public void getTransaction_NotFound() {
        // Arrange
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchTransactionException.class, () -> transactionService.getTransaction("1"));
    }

    @Test
    public void getAllHoldings_Success() {
        // Arrange
        List<HoldingDto> expectedHoldings = List.of(buildValidHoldingDto());

        when(transactionRepository.findAll()).thenReturn(List.of(buildValidTransactionEntity()));
        when(transactionMapper.TransactionEntityToDto(any())).thenReturn(buildValidTransactionDto());
        when(cryptoWebSocketService.getPrices()).thenReturn(buildValidCryptoPrices());
        when(transactionMapper.mapTransactionToHoldingDto(anyList(), anyMap())).thenReturn(expectedHoldings);

        // Act
        List<HoldingDto> actualHoldings = transactionService.getAllHoldings();

        // Assert
        assertEquals(actualHoldings.size(), expectedHoldings.size());
        assertEquals(actualHoldings.get(0).getCryptoSymbol(), expectedHoldings.get(0).getCryptoSymbol());
    }
}
