package com.lending.application.facade;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.domain.dto.TransactionDto;
import com.lending.application.mapper.TransactionMapper;
import com.lending.application.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceFacade {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public void createTransaction(Account account, final BigDecimal amount, final TransactionMethodEnum methodEnum) {
        Transaction transaction = new Transaction(amount, LocalDate.now(), methodEnum);
        transaction.setAccount(account);
        account.getTransactionList().add(transaction);
    }

    public List<TransactionDto> getAllAccountTransactions(final Long accountId) {
        List<Transaction> transactionList = transactionService.findAllByAccountId(accountId);

        return transactionMapper.mapToTransactionDtoList(transactionList);
    }

    public List<TransactionDto> getAllTransactionsByMethod(final TransactionMethodEnum method, final Long accountId) {
        List<Transaction> transactionList = transactionService.findTransactionsByMethodAndAccount(method, accountId);

        return transactionMapper.mapToTransactionDtoList(transactionList);
    }
}
