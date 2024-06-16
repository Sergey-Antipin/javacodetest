package com.antipin.javacodetest.util;

import com.antipin.javacodetest.dto.WalletDto;
import com.antipin.javacodetest.model.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public Wallet dtoToWallet(WalletDto dto) {
        return new Wallet(dto.getId(), dto.getBalance());
    }

    public WalletDto walletToDto(Wallet wallet) {
        return new WalletDto(wallet.getId(), wallet.getBalance());
    }
}
