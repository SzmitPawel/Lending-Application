package com.lending.application.service.transaction;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.dto.TransactionDto;
import com.lending.application.exception.TransactionNotFoundException;
import com.lending.application.mapper.TransactionMapper;
import com.lending.application.repository.CreditRatingRepository;
import com.lending.application.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TransactionService {
    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;
    private final CreditRatingRepository creditRatingRepository;

    public void createTransaction(final TransactionDto transactionDto) {
        transactionRepository.saveAndFlush(transactionMapper.mapToTransaction(transactionDto));
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

    public void updateTransaction(final TransactionDto transactionDto) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository
                .findById(transactionDto.getTransactionID())
                .orElseThrow(TransactionNotFoundException::new);

        transaction.setTransactionAmount(transactionDto.getTransactionAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setTransactionMethodEnum(transactionDto.getTransactionMethodEnum());

        transactionRepository.saveAndFlush(transaction);
    }

    public void deleteTransactionById(final Long transactionId) throws TransactionNotFoundException {
        if (transactionRepository.findById(transactionId).isPresent()) {
            transactionRepository.deleteById(transactionId);
        } else {
            throw new TransactionNotFoundException();
        }
    }
}