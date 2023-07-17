package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
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

    public void createAccount(final AccountDto accountDto) {
        Account account = accountMapper.mapToAccount(accountDto);
        accountRepository.saveAndFlush(account);
    }

    public AccountDto getAccountById(final Long id) throws ClientNotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        return accountMapper.mapToDto(account);
    }

    public void deleteAccount(final Long id) throws ClientNotFoundException {
        if (accountRepository.findById(id).isPresent()) {
            accountRepository.deleteById(id);
        } else {
            throw new ClientNotFoundException();
        }
    }

    public AccountDto updateAccountBalance(final Long id, final BigDecimal balance) throws ClientNotFoundException {
        Account account = accountRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        account.setBalance(balance);
        accountRepository.saveAndFlush(account);

        return accountMapper.mapToDto(account);
    }
}