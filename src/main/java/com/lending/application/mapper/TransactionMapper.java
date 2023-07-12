package com.lending.application.mapper;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {
    public TransactionDto mapToTransactionDto(final Transaction transaction) {
        return new TransactionDto(
                transaction.getTransactionID(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate(),
                transaction.getTransactionMethodEnum()
        );
    }

    public Transaction mapToTransaction(final TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setTransactionID(transactionDto.getTransactionID());
        transaction.setTransactionAmount(transactionDto.getTransactionAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setTransactionMethodEnum(transactionDto.getTransactionMethodEnum());

        return transaction;
    }

    public List<TransactionDto> mapToTransactionDtoList(final List<Transaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToTransactionDto)
                .collect(Collectors.toList());
    }

    public List<Transaction> mapToTransactionList(final List<TransactionDto> transactionDtoList) {
        return transactionDtoList.stream()
                .map(this::mapToTransaction)
                .collect(Collectors.toList());
    }
}