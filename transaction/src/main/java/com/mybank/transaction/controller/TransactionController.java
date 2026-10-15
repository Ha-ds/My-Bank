package com.mybank.transaction.controller;

import com.mybank.transaction.dto.TransferDto;
import com.mybank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferDto transferDto) {
        transactionService.transfer(transferDto);

    }
}
