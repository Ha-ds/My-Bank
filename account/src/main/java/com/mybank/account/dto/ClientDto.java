package com.mybank.account.dto;

import java.math.BigDecimal;

public record ClientDto(
        String name,
        String iban,
        BigDecimal balance
) {
}
