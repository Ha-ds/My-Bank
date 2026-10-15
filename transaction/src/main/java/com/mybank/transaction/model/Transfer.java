package com.mybank.transaction.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Transfer extends Transaction {

    String sourceClientIban;
    String targetClientIban;

    public Transfer(TransactionType type,
                    TransactionStatus status,
                    BigDecimal amount,
                    String sourceClientIban,
                    String targetClientIban) {
        super(type, status, amount);
        this.sourceClientIban = sourceClientIban;
        this.targetClientIban = targetClientIban;
    }
}
