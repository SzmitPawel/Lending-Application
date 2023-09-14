package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testGetAccountById_AccountNotFoundException() {
        // given
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testSaveAccount() {
        // given
        Account account = new Account();

        when(accountRepository.saveAndFlush(account)).thenReturn(account);

        // when
        Account retrievedAccount = accountService.saveAccount(account);

        // then
        verify(accountRepository,times(1)).saveAndFlush(any(Account.class));

        assertNotNull(retrievedAccount);
    }

    @Test
    void testGetAccountById() throws AccountNotFoundException {
        // given
        Account account = new Account();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        // when
        Account retrievedAccountDto = accountService.getAccountById(1L);

        // then
        verify(accountRepository,times(1)).findById(any());

        assertNotNull(retrievedAccountDto);
    }

    @Test
    void testDeleteAccountById_AccountNotFoundException() {
        // given
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(1L));
    }

    @Test
    void testDeleteAccountById() throws AccountNotFoundException {
        // given
        Account account = new Account();

        when(accountRepository.findById(any())).thenReturn(Optional.of(account));

        accountService.deleteAccount(1L);

        // when & then
        verify(accountRepository, times(1)).deleteById(any());
    }
}