package com.antipin.javacodetest.exception;

import com.antipin.javacodetest.dto.OperationDto;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(BigDecimal accountBalance, OperationDto operation) {
        super("Cannot process operation: the wallet with UUID " + operation.getWalletId()
        + " has insufficient funds. Required balance: " + operation.getAmount()
        + ", current balance: " + accountBalance);
    }
}
