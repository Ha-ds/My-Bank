package com.mybank.transaction.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Data
public class Client {

    String clientId;
    String iban;
}
