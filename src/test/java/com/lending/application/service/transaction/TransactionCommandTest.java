package com.lending.application.service.transaction;

import com.lending.application.domain.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.service.account.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionCommandTest {
    @InjectMocks
    private TransactionCommand transactionCommand;
    @Mock
    private AccountService accountService;

    @Test
    void transaction_should_return_transaction_if_succeed() {
        // given
        Account account = new Account(BigDecimal.TEN);
        BigDecimal transactionAmount = BigDecimal.ONE;
        TransactionMethodEnum methodEnum = TransactionMethodEnum.DEPOSIT;

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setTransactionDate(LocalDate.now());
        expectedTransaction.setTransactionMethodEnum(methodEnum);
        expectedTransaction.setTransactionAmount(transactionAmount);

        when(accountService.saveAccount(account)).thenReturn(account);

        // when
        Transaction retrievedTransaction = transactionCommand.doTransaction(account,transactionAmount,methodEnum);

        // then
        verify(accountService,times(1)).saveAccount(account);

        assertNotNull(retrievedTransaction);
        assertEquals(expectedTransaction.getTransactionAmount(),retrievedTransaction.getTransactionAmount());
        assertEquals(expectedTransaction.getTransactionDate(),retrievedTransaction.getTransactionDate());
        assertEquals(expectedTransaction.getTransactionMethodEnum(),retrievedTransaction.getTransactionMethodEnum());
    }

    @Test
    void transaction_should_return_transaction_and_be_correctly_associated_with_account() {
        // given
        Account account = new Account(BigDecimal.TEN);
        BigDecimal transactionAmount = BigDecimal.ONE;
        TransactionMethodEnum methodEnum = TransactionMethodEnum.WITHDRAWAL;

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setTransactionDate(LocalDate.now());
        expectedTransaction.setTransactionMethodEnum(methodEnum);
        expectedTransaction.setTransactionAmount(transactionAmount);

        when(accountService.saveAccount(account)).thenReturn(account);

        // when
        Transaction retrievedTransaction = transactionCommand.doTransaction(account,transactionAmount,methodEnum);

        // then
        assertNotNull(retrievedTransaction);
        assertTrue(account.getTransactionList().contains(retrievedTransaction));
    }
}