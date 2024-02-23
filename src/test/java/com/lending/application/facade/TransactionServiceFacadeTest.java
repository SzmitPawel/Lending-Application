package com.lending.application.facade;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.mapper.transaction.TransactionResponseMapper;
import com.lending.application.service.transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionServiceFacadeTest {
    @InjectMocks
    private TransactionServiceFacade transactionServiceFacade;
    @Mock
    private TransactionService transactionService;
    @Mock
    private TransactionResponseMapper responseMapper;

    @Test
    void create_transaction_should_add_transaction_for_deposit() {
        // given
        Account account = new Account(BigDecimal.ZERO);
        BigDecimal deposit = BigDecimal.valueOf(100.00);
        TransactionMethodEnum methodEnum = TransactionMethodEnum.DEPOSIT;

        // while
        transactionServiceFacade.createTransaction(account, deposit, methodEnum);

        // then
        assertEquals(deposit, account.getTransactionList().get(0).getTransactionAmount());
        assertEquals(methodEnum, account.getTransactionList().get(0).getTransactionMethodEnum());
        assertEquals(LocalDate.now(), account.getTransactionList().get(0).getTransactionDate());
    }

    @Test
    void create_transaction_should_add_transaction_for_withdraw() {
        // given
        Account account = new Account(BigDecimal.ZERO);
        BigDecimal deposit = BigDecimal.valueOf(100.00);
        TransactionMethodEnum methodEnum = TransactionMethodEnum.WITHDRAWAL;

        // while
        transactionServiceFacade.createTransaction(account, deposit, methodEnum);

        // then
        assertEquals(deposit, account.getTransactionList().get(0).getTransactionAmount());
        assertEquals(methodEnum, account.getTransactionList().get(0).getTransactionMethodEnum());
        assertEquals(LocalDate.now(), account.getTransactionList().get(0).getTransactionDate());
    }
}