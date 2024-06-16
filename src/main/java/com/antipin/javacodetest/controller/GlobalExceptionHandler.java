package com.antipin.javacodetest.controller;

import com.antipin.javacodetest.exception.AppError;
import com.antipin.javacodetest.exception.InsufficientFundsException;
import com.antipin.javacodetest.exception.UnsupportedOperationTypeException;
import com.antipin.javacodetest.exception.WalletNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<?> handleInsufficientFundsException(InsufficientFundsException e) {
        logger.warn("Insufficient funds: {}", e.getMessage());
        return new ResponseEntity<>(
                new AppError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationTypeException.class)
    public ResponseEntity<?> handleUnsupportedOperationTypeException(UnsupportedOperationTypeException e) {
        return new ResponseEntity<>(
                new AppError(e.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<?> handleWalletNotFoundException(WalletNotFoundException e) {
        logger.warn("Wallet not found: {}", e.getMessage());
        return new ResponseEntity<>(
                new AppError(e.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.warn("Cannot parse JSON: {}", ex.getMessage());
        return new ResponseEntity<>(
                new AppError("Invalid JSON format: " + ex.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
