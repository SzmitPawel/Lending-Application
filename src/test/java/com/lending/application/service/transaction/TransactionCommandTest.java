package com.lending.application.service.transaction;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.transaction.Transaction;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionCommandTest {
    @Autowired
    private TransactionCommand transactionCommand;

    @Test
    void transaction_should_create_transaction_if_succeed() {
        // given
        Account account = new Account(BigDecimal.TEN);
        BigDecimal transactionAmount = BigDecimal.ONE;
        TransactionMethodEnum methodEnum = TransactionMethodEnum.DEPOSIT;

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setTransactionDate(LocalDate.now());
        expectedTransaction.setTransactionMethodEnum(methodEnum);
        expectedTransaction.setTransactionAmount(transactionAmount);

        // when
        transactionCommand.doTransaction(account,transactionAmount,methodEnum);

        // then
        assertEquals(expectedTransaction.getTransactionAmount(),
                account.getTransactionList().get(0).getTransactionAmount());
        assertEquals(expectedTransaction.getTransactionDate(),
                account.getTransactionList().get(0).getTransactionDate());
        assertEquals(expectedTransaction.getTransactionMethodEnum()
                ,account.getTransactionList().get(0).getTransactionMethodEnum());
    }

    @Test
    void transaction_should_return_transaction_and_be_correctly_associated_with_account() {
        // given
        Account account = new Account(BigDecimal.TEN);
        BigDecimal transactionAmount = BigDecimal.ONE;
        TransactionMethodEnum methodEnum = TransactionMethodEnum.WITHDRAWAL;

        // when
        transactionCommand.doTransaction(account,transactionAmount,methodEnum);

        // then
        assertEquals(1,account.getTransactionList().size());
        assertEquals(account,account.getTransactionList().get(0).getAccount());
    }
}