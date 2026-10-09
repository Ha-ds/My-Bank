package com.mybank.account.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Account {

    AccountType accountType;

    BigDecimal amount = BigDecimal.valueOf(0);

    public Account(AccountType accountType) {
        this.accountType = accountType;
    }
}
