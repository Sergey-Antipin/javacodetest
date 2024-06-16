package com.antipin.javacodetest.repository;

import com.antipin.javacodetest.exception.WalletNotFoundException;
import com.antipin.javacodetest.model.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("walletRepository")
public class WalletRepositoryImpl implements WalletRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WalletRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Wallet get(UUID id) {
        String sql = "SELECT id, balance FROM wallets w WHERE w.id = ?";
        return executeSelect(sql, id);
    }

    public Wallet getWithRowLock(UUID id) {
        String sql = "SELECT id, balance FROM wallets w WHERE w.id = ? FOR UPDATE";
        return executeSelect(sql, id);
    }

    @Override
    public void update(Wallet wallet) {
        String sql = "UPDATE wallets SET balance = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, wallet.getBalance(), wallet.getId());
        if (rowsAffected == 0) {
            throw new WalletNotFoundException(wallet.getId());
        }
    }

    private Wallet executeSelect(String sql, UUID id) {
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id},
                    (rs, rowNum) -> new Wallet(
                            rs.getObject(1, UUID.class),
                            rs.getBigDecimal(2)));
        } catch (EmptyResultDataAccessException e) {
            throw new WalletNotFoundException(id);
        }
    }
}