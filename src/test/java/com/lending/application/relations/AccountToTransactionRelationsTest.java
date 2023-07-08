package com.lending.application.relations;

import com.lending.application.domain.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class AccountToTransactionRelationsTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void createAccountWithTransaction_shouldReturn1AccountAnd1Transaction() {
        // given
        Account account = new Account(new BigDecimal(100.00));
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT);

        account.getTransactionList().add(transaction);
        transaction.setAccount(account);

        accountRepository.saveAndFlush(account);

        // when & then
        assertEquals(1, accountRepository.count());
        assertEquals(1, transactionRepository.count());
    }

    @Test
    void deleteTransaction_shouldReturn1AccountAnd0Transactions() {
        // given
        Account account = new Account(new BigDecimal(100.00));
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT);

        account.getTransactionList().add(transaction);
        transaction.setAccount(account);
        accountRepository.saveAndFlush(account);

        // when
        account = accountRepository.findAll().get(0);
        transaction = account.getTransactionList().get(0);
        account.getTransactionList().remove(0);
        transactionRepository.delete(transaction);
        accountRepository.saveAndFlush(account);

        // then
        assertEquals(1, accountRepository.count());
        assertEquals(0, transactionRepository.count());
    }

    @Test
    void deleteAccountWithTransactions_shouldReturn0AccountsAnd0Transactions() {
        // given
        Account account = new Account(new BigDecimal(0.00));
        Transaction transaction1 = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        Transaction transaction2 = new Transaction(
                new BigDecimal(200.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        account.getTransactionList().addAll(Arrays.asList(transaction1,transaction2));
        transaction1.setAccount(account);
        transaction2.setAccount(account);
        accountRepository.saveAndFlush(account);

        // when
        accountRepository.deleteAll();

        // then
        assertEquals(0, accountRepository.count());
        assertEquals(0, transactionRepository.count());
    }

    @Test
    void updateTransaction_shouldReturnUpdatedData() {
        // given
        Account account = new Account(new BigDecimal(0.00));
        Transaction transaction1 = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        Transaction transaction2 = new Transaction(
                new BigDecimal(200.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        account.getTransactionList().addAll(Arrays.asList(transaction1,transaction2));
        transaction1.setAccount(account);
        transaction2.setAccount(account);
        accountRepository.saveAndFlush(account);

        // when
        transaction2.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);
        transaction2.setPaymentAmount(new BigDecimal(50.00));

        // then
        assertEquals(TransactionMethodEnum.WITHDRAWAL, accountRepository.findAll()
                .get(0)
                .getTransactionList()
                .get(1)
                .getTransactionMethodEnum());
        assertEquals(new BigDecimal(50.00), accountRepository.findAll()
                .get(0)
                .getTransactionList()
                .get(1)
                .getPaymentAmount());
    }

    @Test
    void readTransactionsFromAccount_shouldReturnAllData() {
        // given
        Account account = new Account(new BigDecimal(100.00));
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT);

        account.getTransactionList().add(transaction);
        transaction.setAccount(account);

        accountRepository.saveAndFlush(account);

        // when & then
        assertEquals(new BigDecimal(100.00), accountRepository.findAll()
                .get(0)
                .getTransactionList()
                .get(0)
                .getPaymentAmount());
        assertEquals(LocalDate.now(), accountRepository.findAll()
                .get(0)
                .getTransactionList()
                .get(0)
                .getPaymentDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, accountRepository.findAll()
                .get(0)
                .getTransactionList()
                .get(0)
                .getTransactionMethodEnum());
    }
}