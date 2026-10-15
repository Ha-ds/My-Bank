package com.mybank.transaction.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Transaction {

    private String transactionId;
    private TransactionType type;
    private TransactionStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Transaction(TransactionType type, TransactionStatus status, BigDecimal amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }
}
