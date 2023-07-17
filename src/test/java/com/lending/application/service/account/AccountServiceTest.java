package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
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
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testCreateAccountById() {
        // given
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal(10));

        AccountDto accountDto = new AccountDto(1L, new BigDecimal(10));

        when(accountMapper.mapToAccount(accountDto)).thenReturn(account);
        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        // when
        accountService.createAccount(accountDto);


        // then
        verify(accountMapper, times(1)).mapToAccount(accountDto);
        verify(accountRepository, times(1)).saveAndFlush(account);
    }

    @Test
    void testGetAccount_shouldReturnAccountDto() throws ClientNotFoundException {
        // given
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal(10));

        AccountDto accountDto = new AccountDto(1L, new BigDecimal(10));

        when(accountMapper.mapToDto(account)).thenReturn(accountDto);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // when
        AccountDto retrievedAccountDto = accountService.getAccountById(1L);

        // then
        assertEquals(accountDto, retrievedAccountDto);
    }

    @Test
    void testUpdateAccountBalance_shouldReturnUpdateAccount() throws ClientNotFoundException {
        // given
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal(10));

        AccountDto accountDto = new AccountDto(1L, new BigDecimal(20));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.mapToDto(account)).thenReturn(accountDto);

        // when
        AccountDto retrievedAccountAfterUpdate = accountService.updateAccountBalance(1L, new BigDecimal(20));

        // then
        assertEquals(1L, retrievedAccountAfterUpdate.getAccountId());
        assertEquals(new BigDecimal(20), retrievedAccountAfterUpdate.getBalance());
        verify(accountMapper, times(1)).mapToDto(account);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteAccountById() throws ClientNotFoundException {
        // given
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(new BigDecimal(10));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        // when & then
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).deleteById(1L);
    }
}