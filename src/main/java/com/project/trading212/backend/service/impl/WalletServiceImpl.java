package com.project.trading212.backend.service.impl;

import com.project.trading212.backend.exception.NoSuchWalletException;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;
import com.project.trading212.backend.repository.TransactionRepository;
import com.project.trading212.backend.repository.WalletRepository;
import com.project.trading212.backend.service.WalletService;
import com.project.trading212.backend.util.mapper.WalletMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.NO_SUCH_WALLET_EXCEPTION_DESCRIPTION;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final WalletMapper walletMapper;

    @Override
    public WalletResponseDto createWallet(WalletRequestDto request) {
        WalletEntity walletEntity = walletRepository.save(walletMapper.walletDtoToEntity(request));
        return walletMapper.walletEntityToDto(walletEntity);
    }


    @Override
    public WalletResponseDto getWallet(String walletId) {
        WalletEntity walletEntity = walletRepository.findById(Long.parseLong(walletId)).orElseThrow(() -> new NoSuchWalletException(NO_SUCH_WALLET_EXCEPTION_DESCRIPTION));
        return walletMapper.walletEntityToDto(walletEntity);
    }

    @Override
    public WalletResponseDto resetWallet(WalletRequestDto newWallet, String walletId) {
        WalletEntity existingWallet = walletRepository.findById(Long.parseLong(walletId)).orElseThrow(() -> new NoSuchWalletException(NO_SUCH_WALLET_EXCEPTION_DESCRIPTION));
        String originalWalletName = existingWallet.getWalletName();
        WalletEntity updatedWallet = WalletEntity.builder().id(existingWallet.getId()).walletName(newWallet.getWalletName()).accountBalance(newWallet.getWalletBalance()).build();
        WalletEntity savedWallet = walletRepository.save(updatedWallet);
        int deletedCount = transactionRepository.deleteAllByWallet_WalletName(originalWalletName);
        log.info("Reset wallet with ID: {}. Deleted {} transactions.", walletId, deletedCount);

        return walletMapper.walletEntityToDto(savedWallet);
    }
}
