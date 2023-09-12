package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(final Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public Account getAccountById(final Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
    }

    public Account updateAccountBalance(final Long accountId, final BigDecimal balance) throws AccountNotFoundException {
        Account retrievedAccount = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        retrievedAccount.setBalance(balance);

        return accountRepository.saveAndFlush(retrievedAccount);
    }

    public void deleteAccount(final Long accountId) throws AccountNotFoundException {
        if (accountRepository.findById(accountId).isPresent()) {
            accountRepository.deleteById(accountId);
        } else {
            throw new AccountNotFoundException();
        }
    }
}