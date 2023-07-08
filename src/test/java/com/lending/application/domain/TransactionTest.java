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
    void createTransaction_shouldReturn1() {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(100.00),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );

        transactionRepository.saveAndFlush(transaction);

        // when & then
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

        // then
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
        transaction.setPaymentAmount(new BigDecimal(200.00));
        transaction.setPaymentDate(LocalDate.of(2023,01,01));
        transactionRepository.saveAndFlush(transaction);

        // then
        assertEquals(TransactionMethodEnum.WITHDRAWAL, transactionRepository.findAll()
                .get(0)
                .getTransactionMethodEnum());
        assertEquals(new BigDecimal(200.00), transactionRepository.findAll()
                .get(0)
                .getPaymentAmount());
        assertEquals(LocalDate.of(2023,01,01), transactionRepository.findAll()
                .get(0)
                .getPaymentDate());
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

        // when & then
        assertEquals(new BigDecimal(100.00), transactionRepository.findAll()
                .get(0)
                .getPaymentAmount());
        assertEquals(LocalDate.now(), transactionRepository.findAll()
                .get(0)
                .getPaymentDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, transactionRepository.findAll()
                .get(0)
                .getTransactionMethodEnum());
    }

}