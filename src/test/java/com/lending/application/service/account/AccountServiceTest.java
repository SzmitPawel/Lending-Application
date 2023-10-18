package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    private Account prepareAccount() {
        Account account = new Account(BigDecimal.ZERO);

        return account;
    }

    @Test
    public void testGetAccountById_shouldThrowAccountNotFoundException() {
        // given
        Long accountID = 999L;

        // when & then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(accountID));
    }

    @Test
    void testSaveAccount_shouldSaveAccount() {
        // given
        Account account = accountRepository.saveAndFlush(prepareAccount());

        // when
        Account retrievedAccount = accountService.saveAccount(account);

        // then
        assertNotNull(retrievedAccount);
        assertEquals(account.getBalance(), retrievedAccount.getBalance());
    }

    @Test
    void testGetAccountById_shouldReturnAccount() throws AccountNotFoundException {
        // given
        Account account = accountRepository.saveAndFlush(prepareAccount());

        // when
        Account retrievedAccount = accountService.getAccountById(account.getAccountId());

        // then
        assertNotNull(retrievedAccount);
        assertEquals(account.getBalance(),retrievedAccount.getBalance());
    }

    @Test
    void testDeleteAccountById_shouldThrowAccountNotFoundException() {
        // given
        Long accountId = 999L;

        // when & then
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(accountId));
    }

    @Test
    void testDeleteAccountById() throws AccountNotFoundException {
        // given
        Account account = accountRepository.saveAndFlush(prepareAccount());

        // when
        accountService.deleteAccount(account.getAccountId());

        // & then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(account.getAccountId()));
    }
}