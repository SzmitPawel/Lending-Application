package com.lending.application.mapper;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TransactionMapper {
    AccountMapper accountMapper;

    public TransactionDto mapToTransactionDto(final Transaction transaction) {
        return new TransactionDto(
                transaction.getPaymentID(),
                transaction.getPaymentAmount(),
                transaction.getPaymentDate(),
                transaction.getTransactionMethodEnum(),
                accountMapper.mapToAccountDto(transaction.getAccount())
        );
    }

    public Transaction mapToTransaction(final TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.getPaymentID(),
                transactionDto.getPaymentAmount(),
                transactionDto.getPaymentDate(),
                transactionDto.getTransactionMethodEnum(),
                accountMapper.mapToAccount(transactionDto.getAccountDto())
        );
    }

    public List<Transaction> mapToListTransactions(final List<TransactionDto> transactionDtoList) {
        return transactionDtoList.stream()
                .map(this::mapToTransaction)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> mapToListTransactionDto(final List<Transaction> transactionList) {
        return transactionList.stream()
                .map(this::mapToTransactionDto)
                .collect(Collectors.toList());
    }
}