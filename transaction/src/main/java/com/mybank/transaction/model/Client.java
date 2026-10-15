package com.mybank.transaction.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Data
public class Client {

    String clientId;
    String name;
    String iban;
    BigDecimal balance;
}
