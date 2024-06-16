package com.antipin.javacodetest.service;

import com.antipin.javacodetest.dto.OperationDto;
import com.antipin.javacodetest.dto.OperationType;
import com.antipin.javacodetest.dto.WalletDto;
import com.antipin.javacodetest.exception.InsufficientFundsException;
import com.antipin.javacodetest.exception.UnsupportedOperationTypeException;
import com.antipin.javacodetest.model.Wallet;
import com.antipin.javacodetest.repository.WalletRepository;
import com.antipin.javacodetest.util.WalletMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service("walletService")
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletMapper mapper;
    private final WalletRepository repository;

    @Autowired
    public WalletServiceImpl(WalletMapper walletMapper, WalletRepository repository) {
        this.mapper = walletMapper;
        this.repository = repository;
    }

    @Override
    public WalletDto get(UUID uuid) {
        return mapper.walletToDto(repository.get(uuid));
    }

    @Override
    @Transactional
    public void update(OperationDto operation) {
        Wallet wallet = repository.getWithRowLock(operation.getWalletId());
        BigDecimal currentBalance = wallet.getBalance();
        OperationType opType = operation.getOperationType();
        BigDecimal newBalance;
        if (OperationType.WITHDRAW.equals(opType)) {
            checkBalance(currentBalance, operation);
            newBalance = currentBalance.subtract(operation.getAmount());
        } else if (OperationType.DEPOSIT.equals(opType)) {
            newBalance = currentBalance.add(operation.getAmount());
        } else {
            throw new UnsupportedOperationTypeException(operation);
        }
        repository.update(new Wallet(operation.getWalletId(), newBalance));
    }

    private void checkBalance(BigDecimal currentBalance, OperationDto operation) {
        if (currentBalance.compareTo(operation.getAmount()) < 0) {
            throw new InsufficientFundsException(currentBalance, operation);
        }
    }
}
