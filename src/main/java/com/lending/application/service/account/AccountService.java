package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account saveAccount(final Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public Account getAccountById(final Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public void deleteAccount(final Long accountId) throws AccountNotFoundException {
        if (accountRepository.findById(accountId).isPresent()) {
            accountRepository.deleteById(accountId);
        } else {
            throw new AccountNotFoundException();
        }
    }
}