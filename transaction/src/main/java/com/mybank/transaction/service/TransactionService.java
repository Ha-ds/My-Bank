package com.mybank.transaction.service;

import com.mybank.transaction.external.AccountClient;
import com.mybank.transaction.model.Client;
import com.mybank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

}
