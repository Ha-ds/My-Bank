package com.mybank.account.exception;

public class ClientCannotBeNullException extends RuntimeException {
    public ClientCannotBeNullException(String message) {
        super(message);
    }
}
