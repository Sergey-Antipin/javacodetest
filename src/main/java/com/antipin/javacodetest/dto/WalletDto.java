package com.antipin.javacodetest.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class WalletDto {

    private UUID id;

    private BigDecimal balance;


    public WalletDto() {
    }

    public WalletDto(UUID id, BigDecimal balance) {
        this.id = id;
        this.balance = balance.setScale(2, RoundingMode.HALF_EVEN);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, RoundingMode.HALF_EVEN);
    }
}
