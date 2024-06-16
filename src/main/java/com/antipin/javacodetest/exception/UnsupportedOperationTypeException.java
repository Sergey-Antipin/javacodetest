package com.antipin.javacodetest.exception;

import com.antipin.javacodetest.dto.OperationDto;

public class UnsupportedOperationTypeException extends RuntimeException {

    public UnsupportedOperationTypeException(String message) {
        super(message);
    }

    public UnsupportedOperationTypeException(OperationDto operation) {
        super("Unsupported type of operation: " + operation.getOperationType());
    }
}
