package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import com.lending.application.mapper.AccountMapper;
import com.lending.application.repository.AccountRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;

    public AccountDto getAccountById(final Long id) throws ClientNotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        return accountMapper.mapToDto(account);
    }

    public void createAccount(final AccountDto accountDto) {
        Account account = accountMapper.mapToAccount(accountDto);
        accountRepository.saveAndFlush(account);
    }

    public void deleteAccount(final Long id) throws ClientNotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        accountRepository.deleteById(id);
    }

    public void updateAccountBalance(final Long id, final BigDecimal balance) throws ClientNotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        account.setBalance(balance);
        accountRepository.saveAndFlush(account);
    }
}
