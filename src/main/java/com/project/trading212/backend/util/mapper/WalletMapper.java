package com.project.trading212.backend.util.mapper;


import com.project.trading212.backend.model.WalletEntity;
import com.project.trading212.backend.model.dto.request.WalletRequestDto;
import com.project.trading212.backend.model.dto.response.WalletResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WalletMapper {
    public WalletEntity walletDtoToEntity (WalletRequestDto request){

        return WalletEntity.builder()
                .walletName(request.getWalletName())
                .accountBalance(request.getWalletBalance())
                .build();
    }
    public WalletResponseDto walletEntityToDto(WalletEntity entity){
        return WalletResponseDto.builder().walletId(entity.getId())
                .walletName(entity.getWalletName())
                .walletBalance(entity.getAccountBalance())
                .build();
    }
}
