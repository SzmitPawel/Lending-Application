package com.lending.application.service.transaction;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TransactionCommand {

    public void doTransaction(
            Account account,
            final BigDecimal transactionAmount,
            final TransactionMethodEnum methodEnum
    ) {
        makeTransaction(account, createTransaction(transactionAmount,methodEnum));
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
    }
}