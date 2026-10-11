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
    private String sourceAccountId;
    private String targetAccountId;
    private TransactionStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Transaction(String transactionId, TransactionType type, String sourceAccountId,
                       String targetAccountId, BigDecimal amount) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }
}
