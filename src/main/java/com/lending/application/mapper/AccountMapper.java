package com.lending.application.mapper;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDto mapToDto(final Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getBalance()
        );
    }

    public Account mapToAccount(final AccountDto accountDto) {
        Account account = new Account();
        account.setAccountId(accountDto.getAccountId());
        account.setBalance(accountDto.getBalance());

        return account;
    }
}
