package com.lending.application.domain;

import com.lending.application.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TransactionTest {
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void createPaymentWithWithoutAmountDateAndMethod_shouldReturnDataIntegrityViolationException() {
        // given
        Transaction transaction = new Transaction();

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> transactionRepository.saveAndFlush(transaction));
    }

    @Test
    void createTransaction_shouldReturnTransaction() {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        transactionRepository.saveAndFlush(transaction);

        // when
        Transaction retrievedTransaction = transactionRepository
                .findById(transaction.getPaymentID())
                .orElse(null);

        // then
        assertNotNull(retrievedTransaction);
        assertEquals(1, transactionRepository.count());
    }

    @Test
    void deleteTransaction_shouldReturn0() {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        transactionRepository.saveAndFlush(transaction);

        // when
        transactionRepository.delete(transaction);

        Transaction retrievedTransactionAfterDelete = transactionRepository
                .findById(transaction.getPaymentID())
                .orElse(null);

        // then
        assertNull(retrievedTransactionAfterDelete);
        assertEquals(0, transactionRepository.count());
    }

    @Test
    void updateTransaction_shouldReturnUpdatedData() {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        transactionRepository.saveAndFlush(transaction);

        // when
        transaction.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);
        transaction.setTransactionAmount(new BigDecimal(200.00));
        transaction.setTransactionDate(LocalDate.of(2023,01,01));
        transactionRepository.saveAndFlush(transaction);

        Transaction retrievedTransactionAfterUpdate = transactionRepository
                .findById(transaction.getPaymentID())
                .orElse(null);

        // then
        assertNotNull(retrievedTransactionAfterUpdate);
        assertEquals(TransactionMethodEnum.WITHDRAWAL, retrievedTransactionAfterUpdate.getTransactionMethodEnum());
        assertEquals(new BigDecimal(200.00), retrievedTransactionAfterUpdate.getTransactionAmount());
        assertEquals(LocalDate.of(2023,01,01), retrievedTransactionAfterUpdate.getTransactionDate());
    }

    @Test
    void readTransaction_shouldReturnAllData() {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );

        transactionRepository.saveAndFlush(transaction);

        // when
        Transaction retrievedTransaction = transactionRepository
                .findById(transaction.getPaymentID())
                .orElse(null);

        // then
        assertEquals(new BigDecimal(100.00), retrievedTransaction.getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransaction.getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedTransaction.getTransactionMethodEnum());
    }
}