package com.antipin.javacodetest.controller;

import com.antipin.javacodetest.dto.OperationDto;
import com.antipin.javacodetest.dto.WalletDto;
import com.antipin.javacodetest.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final WalletService service;

    @Autowired
    public WalletController(WalletService service) {
        this.service = service;
    }

    @ResponseBody
    @RequestMapping(value = "/wallets/{WALLET_UUID}", method = RequestMethod.GET)
    public ResponseEntity<WalletDto> get(@PathVariable String WALLET_UUID) {
        logger.info("GET {}", WALLET_UUID);
        WalletDto wallet = service.get(UUID.fromString(WALLET_UUID));
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.POST)
    public ResponseEntity<Void> update(@RequestBody OperationDto operation) {
        logger.info("POST {}", operation.toString());
        service.update(operation);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
