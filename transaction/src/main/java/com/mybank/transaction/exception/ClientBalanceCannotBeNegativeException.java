package com.mybank.transaction.exception;

public class ClientBalanceCannotBeNegativeException extends RuntimeException {
    public ClientBalanceCannotBeNegativeException(String message) {
        super(message);
    }
}
