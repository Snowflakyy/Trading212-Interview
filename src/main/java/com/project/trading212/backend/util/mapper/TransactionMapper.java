package com.project.trading212.backend.util.mapper;

import com.project.trading212.backend.model.CryptoWebSocketEntity;
import com.project.trading212.backend.model.TransactionEntity;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.request.CreateTransactionRequest;
import com.project.trading212.backend.model.dto.CryptoWebSocketDto;
import com.project.trading212.backend.model.dto.response.CreateTransactionResponse;
import com.project.trading212.backend.model.dto.HoldingDto;
import com.project.trading212.backend.model.dto.TransactionDto;
import com.project.trading212.backend.model.enumeration.TransactionType;
import com.project.trading212.backend.repository.CryptoWebSocketRepository;
import com.project.trading212.backend.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionMapper {
    private final CryptoMapper cryptoMapper;
    private final CryptoWebSocketRepository cryptoWebSocketRepository;
    private final WalletRepository walletRepository;
    public TransactionEntity createTransactionRequestToEntity(CreateTransactionRequest request){
        CryptoWebSocketEntity cryptoWebSocketEntity = cryptoWebSocketRepository.findBySymbol(request.getCryptoSymbol())
                .orElseThrow(()-> new RuntimeException(("Crypto not found with symbol:"+request.getCryptoSymbol())));
        WalletEntity walletEntity = walletRepository.findByWalletName(request.getWalletName())
                .orElseThrow(()-> new RuntimeException(("Wallet not found with name:"+request.getWalletName())));

        return TransactionEntity.builder()
                        .transactionType(request.getTransactionType())
                                .quantity(request.getQuantity())
                                        .pricePurchased(request.getPricePurchased())
                                                .cryptoWebSocket(cryptoWebSocketEntity)
                                                        .wallet(walletEntity)
                                                            .build();
    }
    public TransactionDto TransactionEntityToDto(TransactionEntity transaction){
        return TransactionDto.builder()
                .transactionType(transaction.getTransactionType())
                .quantity(transaction.getQuantity())
                .pricePurchased(transaction.getPricePurchased())
                .crypto(cryptoMapper.cryptoWebSocketEntityToDto(transaction.getCryptoWebSocket()))
                .createdDate(transaction.getCreatedDate())
                .build();
    }
    public CreateTransactionResponse TransactionEntityToResponseDto(TransactionEntity transaction){
        WalletEntity walletEntity = walletRepository.getReferenceById(transaction.getWallet().getId());
        LocalDateTime createdDate = transaction.getCreatedDate();
        String formattedDate = null;
        if(createdDate !=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formattedDate = formatter.format(createdDate);
        }
        return CreateTransactionResponse.builder()
                .transactionType(transaction.getTransactionType())
                .quantity(transaction.getQuantity())
                .pricePurchased(transaction.getPricePurchased())
                .crypto(cryptoMapper.cryptoWebSocketToSimpleDto(transaction.getCryptoWebSocket()))
                .createdDate(formattedDate)
                .walletName(walletEntity.getWalletName())
                .build();
    }
    public List<HoldingDto> mapTransactionToHoldingDto(List<TransactionDto> transactions){
        Map<String,List<TransactionDto>> transactionsByCryptoId = transactions.stream().collect(Collectors.groupingBy((t -> t.getCrypto().getSymbol())));
                //use TransactionDto
        List<HoldingDto> holdings = new ArrayList<>();

        for(Map.Entry<String,List<TransactionDto>> entry : transactionsByCryptoId.entrySet()){
            List<TransactionDto> cryptoTransactions = entry.getValue();

            if(cryptoTransactions.isEmpty()){
                continue;
                //handle delete later
            }

            TransactionDto firstTransaction = cryptoTransactions.get(0);
            CryptoWebSocketDto crypto = firstTransaction.getCrypto();

            BigDecimal holdingQuantity = BigDecimal.ZERO;
            for(TransactionDto tx : cryptoTransactions){
                if(tx.getTransactionType() == TransactionType.BUY){
                    holdingQuantity = holdingQuantity.add(tx.getQuantity());
                }else {
                    holdingQuantity = holdingQuantity.subtract(tx.getQuantity());
                }
            }
//            BigDecimal holdingQuantity = cryptoTransactions.stream().
//                    map(TransactionDto::getQuantity).
//                    reduce(BigDecimal.ZERO, BigDecimal::add);
            if(holdingQuantity.compareTo(BigDecimal.ZERO)<=0){
                continue;
            }

            BigDecimal totalCost = BigDecimal.ZERO;
            BigDecimal remainingTokens = holdingQuantity;
            List<TransactionDto> buyTransactions = cryptoTransactions.stream()
                    .filter(tx -> tx.getTransactionType() == TransactionType.BUY)
                    .sorted(Comparator.comparing(TransactionDto::getCreatedDate)
                    .reversed()).
                    toList();
            log.debug("buyTransactions: {}", buyTransactions);

            for(TransactionDto buyTx: buyTransactions){
                BigDecimal buyQuantity = buyTx.getQuantity();
                if(remainingTokens.compareTo(buyQuantity)<=0){
                    break;
                }
                BigDecimal usedTokens = remainingTokens.min(buyQuantity);
                totalCost = totalCost.add(
                        usedTokens.multiply(buyTx.getPricePurchased()));
                remainingTokens = remainingTokens.subtract(buyQuantity);
            }

//            BigDecimal holdingCost = cryptoTransactions.stream().
//                    map(t -> t.getQuantity().multiply(t.getPricePurchased())).
//                    reduce(BigDecimal.ZERO,BigDecimal::add);

            BigDecimal currentPrice = crypto.getPrice();
            BigDecimal holdingReturn = currentPrice.multiply(holdingQuantity);

            BigDecimal profitLoss = holdingReturn.subtract(totalCost);

            BigDecimal profitLossPercentage;
            if(totalCost.compareTo(BigDecimal.ZERO)==0){
                profitLossPercentage = BigDecimal.ZERO;
            }
            else{
                profitLossPercentage = profitLoss.divide(totalCost,4, RoundingMode.HALF_UP)
                        .multiply((new BigDecimal("100")));
            }

            HoldingDto holdingDto = HoldingDto.builder()
                    .cryptoName((crypto.getName()))
                    .cryptoSymbol(crypto.getSymbol())
                    .holdingQuantity(holdingQuantity)
                    .holdingReturn(holdingReturn)
                    .profitLoss(profitLoss)
                    .profitLossPercentage(profitLossPercentage)
                    .isProfit(profitLoss.compareTo(BigDecimal.ZERO)>=0)
                    .build();

            holdings.add(holdingDto);

        }
        return holdings;
    }
}
