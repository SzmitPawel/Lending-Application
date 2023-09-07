package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.AccountMapper;
import com.lending.application.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;

    @Test
    public void testGetAccountById_AccountNotFoundException() {
        // given
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testDeleteAccountById_AccountNotFoundException() {
        // given
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(1L));
    }

    @Test
    void testCreateAccount() {
        // given
        AccountDto accountDto = new AccountDto();
        Account account = new Account();

        when(accountMapper.mapToAccount(accountDto)).thenReturn(account);
        when(accountMapper.mapToDto(account)).thenReturn(accountDto);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        // when
        AccountDto retrievedAccount = accountService.createAccount(accountDto);

        // then
        verify(accountMapper,times(1)).mapToAccount(any(AccountDto.class));
        verify(accountMapper,times(1)).mapToDto(any(Account.class));
        verify(accountRepository,times(1)).saveAndFlush(any(Account.class));

        assertNotNull(retrievedAccount);
    }

    @Test
    void testGetAccountById() throws AccountNotFoundException {
        // given
        Account account = new Account();
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.mapToDto(account)).thenReturn(accountDto);

        // when
        AccountDto retrievedAccountDto = accountService.getAccountById(1L);

        // then
        verify(accountRepository,times(1)).findById(any());
        verify(accountMapper,times(1)).mapToDto(any(Account.class));

        assertNotNull(retrievedAccountDto);
    }

    @Test
    void testUpdateAccountBalance() throws AccountNotFoundException {
        // given
        Account account = new Account();
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.saveAndFlush(account)).thenReturn(account);
        when(accountMapper.mapToDto(account)).thenReturn(accountDto);

        // when
        AccountDto updatedAccount = accountService.updateAccountBalance(1L, new BigDecimal(20));

        // then
        verify(accountRepository,times(1)).findById(any());
        verify(accountRepository,times(1)).saveAndFlush(any(Account.class));
        verify(accountMapper,times(1)).mapToDto(any(Account.class));

        assertNotNull(updatedAccount);
    }

    @Test
    void testDeleteAccountById() throws AccountNotFoundException {
        // given
        Account account = new Account();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        // when & then
        verify(accountRepository, times(1)).deleteById(any());
    }
}