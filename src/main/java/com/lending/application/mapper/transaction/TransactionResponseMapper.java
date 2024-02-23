package com.lending.application.mapper.transaction;

import com.lending.application.domain.transaction.Transaction;
import com.lending.application.domain.transaction.TransactionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(implementationPackage = "transaction")
public interface TransactionResponseMapper {
    @Mapping(target = "transactionId", source = "transactionID")
    TransactionResponseDTO mapToTransactionDTO(Transaction transaction);
    List<TransactionResponseDTO> mapToListTransactionDTO(List<Transaction> transactionList);
}
