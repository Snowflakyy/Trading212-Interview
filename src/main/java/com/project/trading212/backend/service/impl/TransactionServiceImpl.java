package com.project.trading212.backend.service.impl;

import com.project.trading212.backend.exception.InsufficientFundsException;
import com.project.trading212.backend.exception.NoSuchWalletException;
import com.project.trading212.backend.model.TransactionEntity;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.TransactionDto;
import com.project.trading212.backend.model.enumeration.TransactionType;
import com.project.trading212.backend.repository.TransactionRepository;
import com.project.trading212.backend.repository.WalletRepository;
import com.project.trading212.backend.service.TransactionService;
import com.project.trading212.backend.util.mapper.TransactionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION;
import static com.project.trading212.backend.exception.ErrorCodeAndMessages.NO_SUCH_WALLET_EXCEPTION_DESCRIPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        String walletName = request.getWalletName();
        String cryptoSymbol = request.getCryptoSymbol();
        BigDecimal quantity = request.getQuantity();
        BigDecimal price = request.getPricePurchased();
        TransactionType type = request.getTransactionType();
        if(type == TransactionType.SELL){
            List<TransactionDto> existingTransactions = transactionRepository.findByWallet_WalletNameAndCryptoWebSocket_Symbol(walletName,cryptoSymbol)
                    .stream().map(transactionMapper::TransactionEntityToDto)
                    .toList();

            List<HoldingDto> holdings = transactionMapper.mapTransactionToHoldingDto(existingTransactions);
            HoldingDto currentHolding = holdings.stream()
                    .filter(h-> h.getCryptoSymbol().equals(cryptoSymbol))
                    .findFirst()
                    .orElse(HoldingDto.builder()
                            .cryptoSymbol(cryptoSymbol)
                            .holdingQuantity(BigDecimal.ZERO)
                            .build());

                if(currentHolding.getHoldingQuantity().compareTo(quantity)<0){
                    throw new InsufficientFundsException(INSUFFICIENT_QUANTITY_EXCEPTION_DESCRIPTION);
                }

                currentHolding.deductQantity(quantity);
        }
        TransactionEntity transactionEntity = transactionRepository.save(transactionMapper.createTransactionRequestToEntity(request));
        processTransaction(walletName,quantity,price,type);
         return transactionMapper.TransactionEntityToResponseDto(transactionEntity);
    }
    private void processTransaction(String walletName, BigDecimal quantity, BigDecimal price,TransactionType type){
        BigDecimal totalAmount = quantity.multiply(price);

        WalletEntity walletEntity = walletRepository.findByWalletName(walletName)
                .orElseThrow(()-> new NoSuchWalletException(NO_SUCH_WALLET_EXCEPTION_DESCRIPTION));
        if(type == TransactionType.SELL){

            walletEntity.credit(totalAmount);
        }else{
        walletEntity.debit(totalAmount);
        }

        walletRepository.save(walletEntity);
    }

    @Override
    public CreateTransactionResponse getTransaction(String transactionId) {
        TransactionEntity transactionEntity = transactionRepository.findById(Long.parseLong(transactionId)).orElseThrow(()-> new RuntimeException("No such transaction"));
        return transactionMapper.TransactionEntityToResponseDto(transactionEntity);
    }

    @Override
    public List<HoldingDto> getAllHoldings() {
        List<TransactionEntity> allTransactionsEntity = transactionRepository.findAll();
        List<TransactionDto> allTransactionDtos = allTransactionsEntity.stream().map(transactionMapper::TransactionEntityToDto).toList();
        return transactionMapper.mapTransactionToHoldingDto(allTransactionDtos);
    }
}
