package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.AccountMapper;
import com.lending.application.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public Account createAccount(final AccountDto accountDto) {
        Account account = accountMapper.mapToAccount(accountDto);
        Account retrievedAccount = accountRepository.saveAndFlush(account);

        return retrievedAccount;
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

    public Account updateAccountBalance(final Long accountId, final BigDecimal balance) throws AccountNotFoundException {
        Account retrievedAccount = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        retrievedAccount.setBalance(balance);

        return accountRepository.saveAndFlush(retrievedAccount);
    }
}