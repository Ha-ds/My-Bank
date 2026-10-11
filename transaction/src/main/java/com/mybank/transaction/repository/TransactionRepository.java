package com.mybank.transaction.repository;

import com.mybank.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionRepository {

  private final List<Transaction> transactions = new ArrayList<>();

  public void save(Transaction transaction) {
      transactions.add(transaction);
  }


}
