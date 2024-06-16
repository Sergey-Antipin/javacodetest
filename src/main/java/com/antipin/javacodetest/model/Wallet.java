package com.antipin.javacodetest.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public class Wallet {

    private UUID id;

    private BigDecimal balance;

    public Wallet() {
    }

    public Wallet(UUID id, BigDecimal balance) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id.equals(wallet.getId()) && balance.compareTo(wallet.getBalance()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance.toPlainString() +
                '}';
    }
}
