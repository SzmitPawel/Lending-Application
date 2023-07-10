package com.lending.application.mapper;

import com.lending.application.domain.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.dto.AccountDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {
    public AccountDto mapToAccountDto(final Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(accountDto.getAccountId());
        accountDto.setBalance(account.getBalance());

        if (account.getTransactionList() != null) {
            List<Long> transactionIds = account.getTransactionList().stream()
                    .map(Transaction::getPaymentID)
                    .collect(Collectors.toList());
            accountDto.setTransactionIds(transactionIds);
        }

        return accountDto;
    }

    public Account mapToAccount(final AccountDto accountDto) {
        Account account = new Account();
        account.setAccountId(accountDto.getAccountId());
        account.setBalance(accountDto.getBalance());

        if (accountDto.getTransactionIds() != null) {
            List<Transaction> transactionList = accountDto.getTransactionIds().stream()
                    .map(transactionId -> {
                        Transaction transaction = new Transaction();
                        transaction.setPaymentID(transactionId);
                        return transaction;
                    })
                    .collect(Collectors.toList());
            account.setTransactionList(transactionList);
        }

        return account;
    }
}
