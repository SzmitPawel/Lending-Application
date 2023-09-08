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
    private TransactionService transactionService;
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
    void testCreateTransaction() {
        // given
        Transaction transaction = new Transaction();
        TransactionDto transactionDto = new TransactionDto();

        when(transactionMapper.mapToTransaction(transactionDto)).thenReturn(transaction);
        when(transactionRepository.saveAndFlush(transaction)).thenReturn(transaction);
        when(transactionMapper.mapToTransactionDto(transaction)).thenReturn(transactionDto);

        // when
        TransactionDto retrievedTransactionDto = transactionService.createTransaction(transactionDto);

        // then
        verify(transactionMapper,times(1)).mapToTransaction(any(TransactionDto.class));
        verify(transactionRepository,times(1)).saveAndFlush(any(Transaction.class));
        verify(transactionMapper,times(1)).mapToTransactionDto(any(Transaction.class));

        assertNotNull(retrievedTransactionDto);
    }

    @Test
    void testGetTransactionById() throws TransactionNotFoundException {
        // given
        Transaction transaction = new Transaction();
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionID(1L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.mapToTransactionDto(transaction)).thenReturn(transactionDto);

        // when
        TransactionDto retrievedTransactionDto = transactionService.getTransactionById(1L);

        // then
        verify(transactionRepository, times(1)).findById(any());
        verify(transactionMapper, times(1)).mapToTransactionDto(any(Transaction.class));

        assertNotNull(retrievedTransactionDto);
    }

    @Test
    void testGetAllTransactions() {
        // given
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();

        List<Transaction> transactionList = List.of(transaction1,transaction2);

        TransactionDto transactionDto1 = new TransactionDto();
        TransactionDto transactionDto2 = new TransactionDto();

        List<TransactionDto> transactionDtoList = List.of(transactionDto1,transactionDto2);

        when(transactionRepository.findAll()).thenReturn(transactionList);
        when(transactionMapper.mapToTransactionDtoList(transactionList)).thenReturn(transactionDtoList);

        // when
        List<TransactionDto> retrievedTransactionDtoList = transactionService.getAllTransactions();

        // then
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).mapToTransactionDtoList(anyList());

        assertNotNull(retrievedTransactionDtoList);
    }

    @Test
    void testUpdateTransaction() throws TransactionNotFoundException {
        // given
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionID(1L);
        Transaction transaction = new Transaction();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.saveAndFlush(transaction)).thenReturn(transaction);
        when(transactionMapper.mapToTransactionDto(transaction)).thenReturn(transactionDto);

        // when
        TransactionDto retrievedTransactionDto = transactionService.updateTransaction(transactionDto);

        // then
        verify(transactionRepository, times(1)).findById(any());
        verify(transactionRepository, times(1)).saveAndFlush(any(Transaction.class));
        verify(transactionMapper,times(1)).mapToTransactionDto(any(Transaction.class));

        assertNotNull(retrievedTransactionDto);
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