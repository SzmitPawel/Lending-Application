package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    AccountService accountService;
    @Mock
    AccountRepository accountRepository;
    @Mock
    AccountMapper accountMapper;

    @Test
    public void testGetAccountById_ClientNotFoundException() {
        // given
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testDeleteAccountById_ClientNotFoundException() {
        // given
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> accountService.deleteAccount(1L));
    }

    @Test
    void testCreateAccount() {
        // given
        AccountDto accountDto = new AccountDto(
                1L,
                new BigDecimal(10)
        );

        Account account = new Account();

        when(accountRepository.saveAndFlush(any(Account.class))).thenReturn(account);
        when(accountMapper.mapToAccount(any(AccountDto.class))).thenReturn(account);
        when(accountMapper.mapToDto(any(Account.class))).thenReturn(accountDto);

        // when
        AccountDto retrievedAccountDto = accountService.createAccount(accountDto);

        // then
        assertEquals(1L, retrievedAccountDto.getAccountId());
        assertEquals(new BigDecimal(10), retrievedAccountDto.getBalance());
        verify(accountRepository, times(1)).saveAndFlush(account);
    }

    @Test
    void testGetAccountById() throws ClientNotFoundException {
        // given
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal(10));

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));
        when(accountMapper.mapToDto(account)).thenCallRealMethod();

        // when
        AccountDto retrievedAccountDto = accountService.getAccountById(1L);

        // then
        verify(accountMapper, times(1)).mapToDto(account);
        verify(accountRepository, times(1)).findById(1L);
        assertEquals(new BigDecimal(10), retrievedAccountDto.getBalance());
    }

    @Test
    void testUpdateAccountBalance() throws ClientNotFoundException {
        // given
        Account account = new Account();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        // when
        accountService.updateAccountBalance(1L, new BigDecimal(20));

        // then
        assertEquals(new BigDecimal(20),account.getBalance());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteAccountById() throws ClientNotFoundException {
        // given
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal(10));

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        // when & then
        verify(accountRepository, times(1)).deleteById(1L);
    }
}