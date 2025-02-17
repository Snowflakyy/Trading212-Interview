package com.project.trading212.backend.service.impl;

import com.project.trading212.backend.exception.NoSuchWalletException;
import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;
import com.project.trading212.backend.repository.WalletRepository;
import com.project.trading212.backend.service.WalletService;
import com.project.trading212.backend.util.mapper.WalletMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.project.trading212.backend.exception.ErrorCodeAndMessages.NO_SUCH_WALLET_EXCEPTION_DESCRIPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    @Override
    public WalletResponseDto createWallet(WalletRequestDto request) {
        WalletEntity walletEntity = walletRepository.save(walletMapper.walletDtoToEntity(request));
        return walletMapper.walletEntityToDto(walletEntity);
    }


    @Override
    public WalletResponseDto getWallet(String walletId) {
        WalletEntity walletEntity = walletRepository.findById(Long.parseLong(walletId)).orElseThrow(()-> new RuntimeException("Wallet not found"));
        return walletMapper.walletEntityToDto(walletEntity);
    }

    @Override
    public WalletResponseDto resetWallet(WalletRequestDto newWallet,String walletId) {
        walletRepository.removeWalletEntityById(Long.parseLong(walletId));
        WalletEntity walletEntity = walletRepository.save(walletMapper.walletDtoToEntity(newWallet));
        return walletMapper.walletEntityToDto(walletEntity);
    }
}
