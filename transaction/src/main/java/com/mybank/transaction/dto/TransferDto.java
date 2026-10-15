package com.mybank.transaction.dto;

import java.math.BigDecimal;

public record TransferDto(
        String sourceClientIban,
        String targetClientIban,
        BigDecimal amount
) {
}
