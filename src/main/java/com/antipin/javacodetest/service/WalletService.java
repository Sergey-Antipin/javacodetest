package com.antipin.javacodetest.service;

import com.antipin.javacodetest.dto.OperationDto;
import com.antipin.javacodetest.dto.WalletDto;

import java.util.UUID;

public interface WalletService {

    WalletDto get(UUID uuid);

    void update(OperationDto operation);
}
