package com.lending.application.controller;

import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.domain.transaction.TransactionResponseDTO;
import com.lending.application.facade.TransactionServiceFacade;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lending/transaction")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TransactionController {
    private final TransactionServiceFacade transactionServiceFacade;
    private final static int MIN_ACCOUNT_VALID = 1;

    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDTO>> getAllAccountTransactions(
            @RequestParam("accountId") @Min(MIN_ACCOUNT_VALID) final Long accountId) {

        List<TransactionResponseDTO> retrievedTransactionResponseDTOList = transactionServiceFacade.getAllAccountTransactions(accountId);
        log.info("Successfully get all transactions for account: " + accountId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedTransactionResponseDTOList);
    }

    @GetMapping("/find")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsByMethod (
            @RequestParam("method") final TransactionMethodEnum method,
            @RequestParam("accountId") @Min(MIN_ACCOUNT_VALID) final Long accountId)
    {

        List<TransactionResponseDTO> retrievedTransactionResponseDTOList = transactionServiceFacade
                .getAllTransactionsByMethod(method,accountId);
        log.info("Successfully get all " + method + " transactions for account: " + accountId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedTransactionResponseDTOList);
    }
}