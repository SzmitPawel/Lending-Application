package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.AccountMapper;
import com.lending.application.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;

    public AccountDto createAccount(final AccountDto accountDto) {
        Account retrievedAccount = accountRepository.saveAndFlush(accountMapper.mapToAccount(accountDto));

        return accountMapper.mapToDto(retrievedAccount);
    }

    public AccountDto getAccountById(final Long accountId) throws ClientNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(ClientNotFoundException::new);
        return accountMapper.mapToDto(account);
    }

    public void deleteAccount(final Long accountId) throws ClientNotFoundException {
        if (accountRepository.findById(accountId).isPresent()) {
            accountRepository.deleteById(accountId);
        } else {
            throw new ClientNotFoundException();
        }
    }

    public void updateAccountBalance(final Long accountId, final BigDecimal balance) throws ClientNotFoundException {
        Account account = accountRepository.findById(accountId).orElseThrow(ClientNotFoundException::new);
        account.setBalance(balance);

        accountRepository.saveAndFlush(account);
    }
}