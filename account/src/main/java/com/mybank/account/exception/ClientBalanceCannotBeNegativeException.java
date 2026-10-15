package com.mybank.account.exception;

public class ClientBalanceCannotBeNegativeException extends RuntimeException {

    public ClientBalanceCannotBeNegativeException(String message) {
        super(message);
    }
}
