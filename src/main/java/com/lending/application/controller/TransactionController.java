package com.lending.application.controller;

import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.domain.dto.TransactionDto;
import com.lending.application.facade.TransactionServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lending/transaction")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionServiceFacade transactionServiceFacade;

    @GetMapping(value = "/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getAllAccountTransactions(@PathVariable final Long accountId) {
        List<TransactionDto> retrievedTransactionDtoList = transactionServiceFacade.getAllAccountTransactions(accountId);
        log.info("Successfully get all transactions for account: " + accountId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedTransactionDtoList);
    }

    @GetMapping(value = "/method/{method}/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByMethod (
            @PathVariable final TransactionMethodEnum method,
            @PathVariable final Long accountId)
    {
        List<TransactionDto> retrievedTransactionDtoList = transactionServiceFacade
                .getAllTransactionsByMethod(method,accountId);
        log.info("Successfully get all " + method + " transactions for account: " + accountId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedTransactionDtoList);
    }
}