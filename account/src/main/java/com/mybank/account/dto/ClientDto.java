package com.mybank.account.dto;

import java.math.BigDecimal;

public record ClientDto(
        String clientId,
        String name,
        String iban,
        BigDecimal amount
) {
}
