package com.lending.application.mapper;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.domain.dto.TransactionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionMapperTest {
    @Autowired
    TransactionMapper transactionMapper;

    @Test
    void TestMapToTransactionDto() {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(1),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        transaction.setTransactionID(1L);

        // when
        TransactionDto retrievedTransactionDto = transactionMapper.mapToTransactionDto(transaction);

        // then
        assertEquals(1L, retrievedTransactionDto.getTransactionID());
        assertEquals(new BigDecimal(1), retrievedTransactionDto.getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransactionDto.getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedTransactionDto.getTransactionMethodEnum());
    }

    @Test
    void TestMapToTransaction() {
        // given
        TransactionDto transactionDto = new TransactionDto(
                1L,
                new BigDecimal(1),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );

        // when
        Transaction retrievedTransaction = transactionMapper.mapToTransaction(transactionDto);

        // then
        assertEquals(1L, retrievedTransaction.getTransactionID());
        assertEquals(new BigDecimal(1), retrievedTransaction.getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransaction.getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedTransaction.getTransactionMethodEnum());
    }

    @Test
    void TestMapToTransactionDtoList() {
        // given
        Transaction transaction1 = new Transaction(
                new BigDecimal(1),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        transaction1.setTransactionID(1L);

        Transaction transaction2 = new Transaction(
                new BigDecimal(2),
                LocalDate.now(),
                TransactionMethodEnum.WITHDRAWAL
        );
        transaction2.setTransactionID(2L);
        List<Transaction> transactionList = List.of(transaction1,transaction2);

        // when
        List<TransactionDto> retrievedTransactionDtoList = transactionMapper.mapToTransactionDtoList(transactionList);

        // then
        assertEquals(2, retrievedTransactionDtoList.size());
        assertEquals(1L, retrievedTransactionDtoList.get(0).getTransactionID());
        assertEquals(new BigDecimal(1), retrievedTransactionDtoList.get(0).getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransactionDtoList.get(0).getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedTransactionDtoList.get(0).getTransactionMethodEnum());
        assertEquals(2L, retrievedTransactionDtoList.get(1).getTransactionID());
        assertEquals(new BigDecimal(2), retrievedTransactionDtoList.get(1).getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransactionDtoList.get(1).getTransactionDate());
        assertEquals(TransactionMethodEnum.WITHDRAWAL, retrievedTransactionDtoList.get(1).getTransactionMethodEnum());
    }

    @Test
    void TestMapToTransactionList() {
        // given
        TransactionDto transactionDto1 = new TransactionDto(
                1L,
                new BigDecimal(1),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        TransactionDto transactionDto2 = new TransactionDto(
                2L,
                new BigDecimal(2),
                LocalDate.now(),
                TransactionMethodEnum.WITHDRAWAL
        );
        List<TransactionDto> transactionDtoList = List.of(transactionDto1,transactionDto2);

        // when
        List<Transaction> retrievedTransactionList = transactionMapper.mapToTransactionList(transactionDtoList);

        // then
        assertEquals(2, retrievedTransactionList.size());
        assertEquals(1L, retrievedTransactionList.get(0).getTransactionID());
        assertEquals(new BigDecimal(1), retrievedTransactionList.get(0).getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransactionList.get(0).getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedTransactionList.get(0).getTransactionMethodEnum());
        assertEquals(2L, retrievedTransactionList.get(1).getTransactionID());
        assertEquals(new BigDecimal(2), retrievedTransactionList.get(1).getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransactionList.get(1).getTransactionDate());
        assertEquals(TransactionMethodEnum.WITHDRAWAL, retrievedTransactionList.get(1).getTransactionMethodEnum());
    }
}