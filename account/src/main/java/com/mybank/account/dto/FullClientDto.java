package com.mybank.account.dto;

import com.mybank.account.model.Account;

import java.math.BigDecimal;
import java.util.List;

public record FullClientDto(
        String name,
        String iban,
        BigDecimal balance,
        List<Account> accounts
) {
}
