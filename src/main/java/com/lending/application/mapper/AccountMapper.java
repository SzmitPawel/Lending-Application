package com.lending.application.mapper;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountMapper {
    TransactionMapper transactionMapper;

    public AccountDto mapToAccountDto(final Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getBalance(),
                transactionMapper.mapToListTransactionDto(account.getTransactionList())
        );
    }

    public Account mapToAccount(final AccountDto accountDto) {
        return new Account(
                accountDto.getAccountId(),
                accountDto.getBalance(),
                transactionMapper.mapToListTransactions(accountDto.getTransactionList())
        );
    }
}