package com.antipin.javacodetest.repository;

import com.antipin.javacodetest.model.Wallet;

import java.util.UUID;

public interface WalletRepository {

    Wallet get(UUID id);

    Wallet getWithRowLock(UUID uuid);

    void update(Wallet wallet);
}
