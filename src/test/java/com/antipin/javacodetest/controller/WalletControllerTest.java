package com.antipin.javacodetest.controller;

import com.antipin.javacodetest.dto.OperationDto;
import com.antipin.javacodetest.dto.OperationType;
import com.antipin.javacodetest.model.Wallet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-app.xml"})
@ActiveProfiles("test")
@Transactional
public class WalletControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private Wallet wallet = new Wallet(UUID.fromString("d2c69875-a9db-4973-8193-3b1296ac281b"),
            BigDecimal.valueOf(1000.00));

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getByUUID() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{WALLET_UUID}", wallet.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(wallet.getId().toString()))
                .andExpect(jsonPath("$.balance").value(wallet.getBalance().doubleValue()));
    }

    @Test
    public void postDepositOperation() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .content(jsonMapper.writeValueAsString(
                                new OperationDto(wallet.getId(),
                                        BigDecimal.valueOf(100.00),
                                        OperationType.DEPOSIT)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    public void postWithdrawOperation() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .content(jsonMapper.writeValueAsString(
                                new OperationDto(wallet.getId(),
                                        BigDecimal.valueOf(100.00),
                                        OperationType.WITHDRAW)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void postIncorrectWallet() throws Exception {
        UUID wrongUUID = UUID.fromString("d2c69875-a9db-4973-8193-3b1296ac2810");
        mockMvc.perform(post("/api/v1/wallet")
                        .content(jsonMapper.writeValueAsString(
                                new OperationDto(wrongUUID,
                                        BigDecimal.valueOf(100.00),
                                        OperationType.WITHDRAW)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Wallet with UUID " + wrongUUID + " not found."));
    }

    @Test
    public void postInsufficientFunds() throws Exception {
        BigDecimal withdraw = BigDecimal.valueOf(2000.00);
        mockMvc.perform(post("/api/v1/wallet")
                        .content(jsonMapper.writeValueAsString(
                                new OperationDto(wallet.getId(),
                                        withdraw,
                                        OperationType.WITHDRAW)
                        )).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Cannot process operation: " +
                                "the wallet with UUID " + wallet.getId() + " has insufficient funds. " +
                                "Required balance: " + withdraw + ", current balance: " + wallet.getBalance()));
    }

    @Test
    public void postInvalidJSON() throws Exception {
        String response = mockMvc.perform(post("/api/v1/wallet")
                        .content("123{\"walletId\": \"d2c69875-a9db-4973-8193-3b1296ac281b\"," +
                                "\"operationType\": \"WITHDRAW\",")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Assert.assertTrue(response.contains("Invalid JSON format: "));
    }
}
