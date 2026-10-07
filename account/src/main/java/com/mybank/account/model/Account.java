package com.mybank.account.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Account {

    AccountType accountType;

    public Account(AccountType accountType) {
        this.accountType = accountType;
    }
}
