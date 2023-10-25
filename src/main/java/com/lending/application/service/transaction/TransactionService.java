package com.lending.application.service.transaction;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.TransactionNotFoundException;
import com.lending.application.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Transaction saveTransaction(final Transaction transaction) {
       return transactionRepository.saveAndFlush(transaction);
    }

    public Transaction getTransactionById(final Long transactionId) throws TransactionNotFoundException {
        return transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
    }

    public void deleteTransactionById(final Long transactionId) throws TransactionNotFoundException {
        if (transactionRepository.findById(transactionId).isPresent()) {
            transactionRepository.deleteById(transactionId);
        } else {
            throw new TransactionNotFoundException();
        }
    }

    public List<Transaction> findAllByAccountId(final Long accountId) {
        return transactionRepository.findAllByAccount_AccountId(accountId);
    }

    public List<Transaction> findTransactionsByMethodAndAccount(
            final TransactionMethodEnum methodEnum, final Long accountId) {
        return transactionRepository
                .findTransactionsByTransactionMethodEnumIsAndAccount_AccountId(methodEnum, accountId);
    }
}