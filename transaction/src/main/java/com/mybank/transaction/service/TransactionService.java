package com.mybank.transaction.service;

import com.mybank.transaction.dto.TransferDto;
import com.mybank.transaction.exception.ClientBalanceCannotBeNegativeException;
import com.mybank.transaction.external.AccountMicroservice;
import com.mybank.transaction.model.*;
import com.mybank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountMicroservice accountMicroservice;

    public void transfer(TransferDto transferDto) {
        log.info("Transfer: Beginning transfer: {}", transferDto);
        Client sourceClient = accountMicroservice.getClientByIban(transferDto.sourceClientIban());
        Client targetClient = accountMicroservice.getClientByIban(transferDto.targetClientIban());

       BigDecimal newSourceBalance = sourceClient.getBalance().subtract(transferDto.amount());
       validateIfBalanceIsStillPositive(newSourceBalance, transferDto);
       BigDecimal newTargetBalance = targetClient.getBalance().add(transferDto.amount());

       sourceClient.setBalance(newSourceBalance);
       targetClient.setBalance(newTargetBalance);

       accountMicroservice.updateClientByIban(sourceClient);
       accountMicroservice.updateClientByIban(targetClient);

       Transaction transfer = new Transfer(
               TransactionType.TRANSFER,
               TransactionStatus.COMPLETED,
               transferDto.amount(),
               transferDto.sourceClientIban(),
               transferDto.targetClientIban());

       transactionRepository.save(transfer);
       log.info("Transfer: Transfer completed successfully");
    }

   private void validateIfBalanceIsStillPositive(BigDecimal newSourceBalance, TransferDto transferDto) {
       if(newSourceBalance.intValue() < 0) {
           log.error("Transfer: Client with iban {} failed to update.Balance cannot be negative", transferDto.sourceClientIban());
           Transaction transfer = new Transfer(
                   TransactionType.TRANSFER,
                   TransactionStatus.FAILED,
                   transferDto.amount(),
                   transferDto.sourceClientIban(),
                   transferDto.targetClientIban());

           transactionRepository.save(transfer);
           throw new ClientBalanceCannotBeNegativeException("Client balance cannot be negative.");
       }
   }
}
