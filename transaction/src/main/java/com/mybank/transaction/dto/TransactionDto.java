package com.mybank.transaction.dto;

import com.mybank.transaction.model.ClientDto;
import com.mybank.transaction.model.Transaction;

public record TransactionDto(

        Transaction transaction,
        ClientDto sourceClient,
        ClientDto targetClient
){
}
