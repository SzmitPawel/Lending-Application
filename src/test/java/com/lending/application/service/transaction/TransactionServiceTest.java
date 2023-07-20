package com.lending.application.service.transaction;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.domain.dto.TransactionDto;
import com.lending.application.exception.TransactionNotFoundException;
import com.lending.application.mapper.TransactionMapper;
import com.lending.application.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    TransactionService transactionService;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionMapper transactionMapper;

    @Test
    void testGetTransactionById_shouldThrowTransactionNotFoundException() {
        // given
        when(transactionRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void testDeleteTransactionById_shouldThrowTransactionNotFoundException() {
        // given
        when(transactionRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransactionById(1L));
    }

    @Test
    void tyestGetTransactionById() throws TransactionNotFoundException {
        // given
        Transaction transaction = new Transaction(
                new BigDecimal(10),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));
        when(transactionMapper.mapToTransactionDto(transaction)).thenCallRealMethod();

        // when
        TransactionDto retrievedTransactionDto = transactionService.getTransactionById(1L);

        // then
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionMapper, times(1)).mapToTransactionDto(transaction);
        assertEquals(new BigDecimal(10), retrievedTransactionDto.getTransactionAmount());
        assertEquals(LocalDate.now(), retrievedTransactionDto.getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, retrievedTransactionDto.getTransactionMethodEnum());
    }

    @Test
    void testGetAllTransactions() {
        // given
        Transaction transaction1 = new Transaction(
                new BigDecimal(10),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );
        Transaction transaction2 = new Transaction(
                new BigDecimal(20),
                LocalDate.now(),
                TransactionMethodEnum.WITHDRAWAL
        );

        List<Transaction> transactionList = List.of(transaction1,transaction2);

        when(transactionRepository.findAll()).thenReturn(transactionList);
        when(transactionMapper.mapToTransactionDtoList(transactionList)).thenCallRealMethod();

        // when
        List<TransactionDto> retrievedTransactionDtoList = transactionService.getAllTransactions();

        // then
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).mapToTransactionDtoList(transactionList);
        assertEquals(2, retrievedTransactionDtoList.size());
    }

    @Test
    void testUpdateTransaction() throws TransactionNotFoundException {
        // given
        TransactionDto transactionDto = new TransactionDto(
                1L,
                new BigDecimal(10),
                LocalDate.now(),
                TransactionMethodEnum.DEPOSIT
        );

        Transaction transaction = new Transaction();

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        // when
        transactionService.updateTransaction(transactionDto);

        // then
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).saveAndFlush(transaction);
        assertEquals(new BigDecimal(10), transaction.getTransactionAmount());
        assertEquals(LocalDate.now(), transaction.getTransactionDate());
        assertEquals(TransactionMethodEnum.DEPOSIT, transaction.getTransactionMethodEnum());
    }

    @Test
    void testDeleteTransactionById() throws TransactionNotFoundException {
        // given
        Transaction transaction = new Transaction();

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        // when
        transactionService.deleteTransactionById(1L);

        // then
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
    }
}