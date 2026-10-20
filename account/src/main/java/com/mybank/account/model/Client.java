package com.mybank.account.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.iban4j.Iban;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mybank.account.model.AccountType.CURRENT;

@Slf4j
@Setter
@Getter
@Data
public class Client {

    String clientId;
    String name;
    String iban;
    String password;
    String email;

    List<Account> accounts = new ArrayList<>();

    public Client(String name, String password, String email) {
        this.clientId = UUID.randomUUID().toString();
        this.name = name;
        this.iban = Iban.random().toString();
        this.password = password;
        this.email = email;
        this.accounts.add(new Account(CURRENT));
    }

    /**
     * This method finds the correct account from the list of accounts using an account type as input
     */
    public Account getAccount(AccountType accountType) {
        for (Account account : accounts) {
            if (account.getAccountType().equals(accountType)) {
                return account;
            }
        }

        log.error("Client: Account type {} does not exist", accountType);
        throw new RuntimeException("Account type does not exist.");
    }
}
