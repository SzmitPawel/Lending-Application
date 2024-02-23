package com.lending.application.relations;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.transaction.Transaction;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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

        // when
        Account retrievedAccount = accountRepository
                .findById(account.getAccountId())
                .orElse(null);

        // when
        assertNotNull(retrievedAccount);
        assertNotNull(retrievedAccount.getTransactionList());
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
        Account retrievedAccount = accountRepository
                .findById(account.getAccountId())
                .orElse(null);

        assertNotNull(retrievedAccount);

        retrievedAccount.getTransactionList().remove(transaction);
        transactionRepository.delete(transaction);
        accountRepository.saveAndFlush(retrievedAccount);

        Account retrievedAccountAfterDelete = accountRepository
                .findById(retrievedAccount.getAccountId())
                .orElse(null);

        // then
        assertNotNull(retrievedAccountAfterDelete);
        assertEquals(0,retrievedAccountAfterDelete.getTransactionList().size());
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
        Account retrievedAccountAfterDelete = accountRepository
                .findById(account.getAccountId())
                .orElse(null);

        // then
        assertNull(retrievedAccountAfterDelete);
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
        Account retrievedAccount = accountRepository
                .findById(account.getAccountId())
                .orElse(null);

        assertNotNull(retrievedAccount);

        retrievedAccount.getTransactionList().get(0).setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);
        retrievedAccount.getTransactionList().get(0).setTransactionAmount(new BigDecimal(50.00));

        Account retrievedAccountAfterUpdate = accountRepository
                .findById(retrievedAccount.getAccountId())
                .orElse(null);

        // then
        assertNotNull(retrievedAccountAfterUpdate);
        assertEquals(TransactionMethodEnum.WITHDRAWAL, retrievedAccountAfterUpdate
                .getTransactionList()
                .get(0)
                .getTransactionMethodEnum());
        assertEquals(new BigDecimal(50.00), retrievedAccountAfterUpdate
                .getTransactionList()
                .get(0)
                .getTransactionAmount());
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

        // when
        Account retrievedAccount = accountRepository
                .findById(account.getAccountId())
                .orElse(null);

        // when & then
        assertNotNull(retrievedAccount);
        assertEquals(new BigDecimal(100.00), retrievedAccount
                .getTransactionList()
                .get(0).getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedAccount
                .getTransactionList()
                .get(0)
                .getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedAccount
                .getTransactionList()
                .get(0)
                .getTransactionMethodEnum());
    }
}