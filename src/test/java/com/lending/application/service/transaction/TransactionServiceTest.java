package com.lending.application.service.transaction;

import com.lending.application.domain.Transaction;
import com.lending.application.exception.TransactionNotFoundException;
import com.lending.application.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;
    @Mock
    TransactionRepository transactionRepository;

    @Test
    void testSaveTransaction() {
        // given
        Transaction transaction = new Transaction();

        when(transactionRepository.saveAndFlush(any(Transaction.class))).thenReturn(transaction);

        // when
        Transaction retrievedTransaction = transactionService.createTransaction(transaction);

        // then
        verify(transactionRepository,times(1)).saveAndFlush(any(Transaction.class));

        assertNotNull(retrievedTransaction);
    }

    @Test
    void testDeleteTransactionById_shouldThrowTransactionNotFoundException() {
        // given
        when(transactionRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransactionById(1L));
    }

    @Test
    void testGetTransactionById() throws TransactionNotFoundException {
        // given
        Transaction transaction = new Transaction();

        when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));

        // when
        Transaction retrievedTransaction = transactionService.getTransactionById(1L);

        // then
        verify(transactionRepository, times(1)).findById(any());

        assertNotNull(retrievedTransaction);
    }

    @Test
    void testGetAllTransactions() {
        // given
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();

        List<Transaction> transactionList = List.of(transaction1,transaction2);

        when(transactionRepository.findAll()).thenReturn(transactionList);

        // when
        List<Transaction> retrievedTransactionList = transactionService.getAllTransactions();

        // then
        verify(transactionRepository, times(1)).findAll();

        assertNotNull(retrievedTransactionList);
    }

    @Test
    void testGetTransactionById_shouldThrowTransactionNotFoundException() {
        // given
        when(transactionRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
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