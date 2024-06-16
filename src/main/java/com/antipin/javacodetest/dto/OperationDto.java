package com.antipin.javacodetest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class OperationDto {

    private UUID walletId;

    private BigDecimal amount;

    private OperationType operationType;

    public OperationDto() {
    }

    public OperationDto(UUID walletId, BigDecimal amount, OperationType operationType) {
        this.walletId = walletId;
        this.amount = amount;
        this.operationType = operationType;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID id) {
        this.walletId = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return "{\"walletId\": \"" + walletId + "\", \"operationType\": \"" + operationType +
                "\", \"amount\": " + amount + "}";
    }
}
