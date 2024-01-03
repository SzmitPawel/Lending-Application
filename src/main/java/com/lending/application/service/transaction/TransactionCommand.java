package com.lending.application.service.transaction;

import com.lending.application.domain.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TransactionCommand {
    private final AccountService accountService;

    public Transaction doTransaction(
            Account account,
            final BigDecimal transactionAmount,
            final TransactionMethodEnum methodEnum
    ) {
        Transaction transaction = createTransaction(transactionAmount, methodEnum);
        makeTransaction(account, transaction);

        return transaction;
    }

    private Transaction createTransaction(
            final BigDecimal transactionAmount,
            final TransactionMethodEnum methodEnum
    ) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionAmount(transactionAmount);
        transaction.setTransactionMethodEnum(methodEnum);

        return transaction;
    }

    private void makeTransaction(
            Account account,
            Transaction transaction
    ) {
        account.getTransactionList().add(transaction);
        transaction.setAccount(account);
        accountService.saveAccount(account);
    }
}