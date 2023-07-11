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
                transaction.getPaymentID(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate(),
                transaction.getTransactionMethodEnum()
        );
    }

    public Transaction mapToTransaction(final TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.getPaymentID(),
                transactionDto.getPaymentAmount(),
                transactionDto.getPaymentDate(),
                transactionDto.getTransactionMethodEnum()
        );
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