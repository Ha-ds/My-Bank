package com.mybank.account.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.mybank.account.model.AccountType.CURRENT;

@Setter
@Getter
@Data
public class Client {

    String clientId;
    String name;
    String iban;
    String password;

    List<Account> accounts = new ArrayList<>();

    public Client(String name, String password) {
        this.name = name;
        this.password = password;
        this.accounts.add(new Account(CURRENT));
    }
}
