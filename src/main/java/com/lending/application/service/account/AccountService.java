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

    public AccountDto createAccount(final AccountDto accountDto) {
        Account account = accountMapper.mapToAccount(accountDto);
        Account retrievedAccount = accountRepository.saveAndFlush(account);

        return accountMapper.mapToDto(retrievedAccount);
    }

    public AccountDto getAccountById(final Long accountId) throws AccountNotFoundException {
        Account retrievedAccount = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

        return accountMapper.mapToDto(retrievedAccount);
    }

    public void deleteAccount(final Long accountId) throws AccountNotFoundException {
        if (accountRepository.findById(accountId).isPresent()) {
            accountRepository.deleteById(accountId);
        } else {
            throw new AccountNotFoundException();
        }
    }

    public AccountDto updateAccountBalance(final Long accountId, final BigDecimal balance) throws AccountNotFoundException {
        Account retrievedAccount = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
        retrievedAccount.setBalance(balance);
        accountRepository.saveAndFlush(retrievedAccount);

        return accountMapper.mapToDto(retrievedAccount);
    }
}