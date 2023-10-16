package com.lending.application.service.transaction;

import com.lending.application.domain.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.TransactionNotFoundException;
import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    private Transaction prepareTransaction(final BigDecimal amount, final TransactionMethodEnum method) {
        Transaction transaction = new Transaction();
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionMethodEnum(method);

        return transaction;
    }

    @Test
    void testGetTransactionById_shouldThrowTransactionNotFoundException() {
        // given
        Long transactionId = 999L;

        // when & then
        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(transactionId));
    }

    @Test
    void testGetTransactionById() throws TransactionNotFoundException {
        // given
        Transaction transaction = transactionRepository
                .saveAndFlush(prepareTransaction(BigDecimal.valueOf(10), TransactionMethodEnum.DEPOSIT));

        // when
        Transaction retrievedTransaction = transactionService.getTransactionById(transaction.getTransactionID());

        // then
        assertNotNull(retrievedTransaction);
        assertEquals(transaction.getTransactionID(), retrievedTransaction.getTransactionID());
        assertEquals(transaction.getTransactionMethodEnum(), retrievedTransaction.getTransactionMethodEnum());
        assertEquals(transaction.getTransactionAmount(), retrievedTransaction.getTransactionAmount());
        assertEquals(transaction.getTransactionDate(), retrievedTransaction.getTransactionDate());
    }

    @Test
    void testDeleteTransactionById_shouldThrowTransactionNotFoundException() {
        // given
        Long transactionId = 999L;

        // when & then
        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.deleteTransactionById(transactionId));
    }

    @Test
    void testDeleteTransactionById() throws TransactionNotFoundException {
        // given
        Transaction transaction = transactionRepository
                .saveAndFlush(prepareTransaction(BigDecimal.valueOf(10), TransactionMethodEnum.DEPOSIT));

        // when
        transactionService.deleteTransactionById(transaction.getTransactionID());

        // then
        assertThrows(TransactionNotFoundException.class,
                () -> transactionService.getTransactionById(transaction.getTransactionID()));
    }

    @Test
    void testFindAllByAccountId_shouldReturnListOfTransactions() {
        // given
        Transaction transaction1 = prepareTransaction(BigDecimal.valueOf(10), TransactionMethodEnum.DEPOSIT);
        Transaction transaction2 = prepareTransaction(BigDecimal.valueOf(20), TransactionMethodEnum.WITHDRAWAL);
        Transaction transaction3 = prepareTransaction(BigDecimal.valueOf(30), TransactionMethodEnum.DEPOSIT);

        Account account = new Account(BigDecimal.valueOf(100));

        account.getTransactionList().add(transaction1);
        account.getTransactionList().add(transaction2);
        account.getTransactionList().add(transaction3);

        transaction1.setAccount(account);
        transaction2.setAccount(account);
        transaction3.setAccount(account);

        Account retrievedAccount = accountRepository.saveAndFlush(account);

        // when
        List<Transaction> retrievedTransactionList = transactionService
                .getAllByAccountId(retrievedAccount.getAccountId());

        // then
        assertNotNull(retrievedTransactionList);
        assertEquals(account.getTransactionList().size(), retrievedTransactionList.size());
        assertEquals(account.getTransactionList().get(0).getTransactionDate(), retrievedTransactionList.get(0).getTransactionDate());
        assertEquals(account.getTransactionList().get(0).getTransactionAmount(), retrievedTransactionList.get(0).getTransactionAmount());
        assertEquals(account.getTransactionList().get(0).getTransactionMethodEnum(), retrievedTransactionList.get(0).getTransactionMethodEnum());

        assertEquals(account.getTransactionList().get(1).getTransactionDate(), retrievedTransactionList.get(1).getTransactionDate());
        assertEquals(account.getTransactionList().get(1).getTransactionAmount(), retrievedTransactionList.get(1).getTransactionAmount());
        assertEquals(account.getTransactionList().get(1).getTransactionMethodEnum(), retrievedTransactionList.get(1).getTransactionMethodEnum());

        assertEquals(account.getTransactionList().get(2).getTransactionDate(), retrievedTransactionList.get(2).getTransactionDate());
        assertEquals(account.getTransactionList().get(2).getTransactionAmount(), retrievedTransactionList.get(2).getTransactionAmount());
        assertEquals(account.getTransactionList().get(2).getTransactionMethodEnum(), retrievedTransactionList.get(2).getTransactionMethodEnum());
    }

    @Test
    void testGetTransactionsByMethodForAccount_shouldReturnCorrectTransactions() {
        // given
        Transaction transaction1 = prepareTransaction(BigDecimal.valueOf(10), TransactionMethodEnum.DEPOSIT);
        Transaction transaction2 = prepareTransaction(BigDecimal.valueOf(20), TransactionMethodEnum.WITHDRAWAL);
        Transaction transaction3 = prepareTransaction(BigDecimal.valueOf(30), TransactionMethodEnum.DEPOSIT);

        Account account = new Account(BigDecimal.valueOf(100));

        account.getTransactionList().add(transaction1);
        account.getTransactionList().add(transaction2);
        account.getTransactionList().add(transaction3);

        transaction1.setAccount(account);
        transaction2.setAccount(account);
        transaction3.setAccount(account);

        TransactionMethodEnum transactionMethod = TransactionMethodEnum.WITHDRAWAL;

        Account retrievedAccount = accountRepository.saveAndFlush(account);

        List<Transaction> expectedTransactionsList = List.of(transaction2);

        // when
        List<Transaction> retrievedTransactionList = transactionService
                .getTransactionsByMethodForAccount(transactionMethod, retrievedAccount.getAccountId());

        // then
        assertNotNull(retrievedTransactionList);
        assertEquals(expectedTransactionsList.size(),retrievedTransactionList.size());
        assertEquals(expectedTransactionsList.get(0).getTransactionMethodEnum(),retrievedTransactionList.get(0).getTransactionMethodEnum());
        assertEquals(expectedTransactionsList.get(0).getTransactionAmount(),retrievedTransactionList.get(0).getTransactionAmount());
        assertEquals(expectedTransactionsList.get(0).getTransactionDate(),retrievedTransactionList.get(0).getTransactionDate());
    }
}