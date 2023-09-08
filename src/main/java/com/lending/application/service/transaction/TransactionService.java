package com.lending.application.service.transaction;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.dto.TransactionDto;
import com.lending.application.exception.TransactionNotFoundException;
import com.lending.application.mapper.TransactionMapper;
import com.lending.application.repository.CreditRatingRepository;
import com.lending.application.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDto createTransaction(final TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.mapToTransaction(transactionDto);
        Transaction createdTransaction = transactionRepository.saveAndFlush(transaction);

        return transactionMapper.mapToTransactionDto(createdTransaction);
    }

    public TransactionDto getTransactionById(final Long transactionId) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);

        return transactionMapper.mapToTransactionDto(transaction);
    }

    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();

        return transactionMapper.mapToTransactionDtoList(transactionList);
    }

    public TransactionDto updateTransaction(final TransactionDto transactionDto) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository
                .findById(transactionDto.getTransactionID())
                .orElseThrow(TransactionNotFoundException::new);

        transaction.setTransactionAmount(transactionDto.getTransactionAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setTransactionMethodEnum(transactionDto.getTransactionMethodEnum());

        Transaction updatedTransaction = transactionRepository.saveAndFlush(transaction);

        return transactionMapper.mapToTransactionDto(updatedTransaction);
    }

    public void deleteTransactionById(final Long transactionId) throws TransactionNotFoundException {
        if (transactionRepository.findById(transactionId).isPresent()) {
            transactionRepository.deleteById(transactionId);
        } else {
            throw new TransactionNotFoundException();
        }
    }
}