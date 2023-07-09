package com.lending.application.domain;

import com.lending.application.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void createAccount_shouldReturnAccount() {
        // given
        Account account = new Account();
        accountRepository.saveAndFlush(account);

        // when
        Account retrievedAccount = accountRepository.findById(account.getAccountId()).orElse(null);

        // then
        assertNotNull(retrievedAccount);
        assertEquals(1,accountRepository.count());
    }

    @Test
    void deleteOneAccount_shouldReturn0() {
        //given
        Account account = new Account();
        accountRepository.saveAndFlush(account);

        // when
        accountRepository.delete(account);
        Account retrievedAccountAfterDelete = accountRepository.findById(account.getAccountId()).orElse(null);

        // then
        assertNull(retrievedAccountAfterDelete);
        assertEquals(0, accountRepository.count());
    }

    @Test
    void updateAccount_shouldReturnUpdatedData() {
        // given
        Account account = new Account(new BigDecimal(100));
        accountRepository.saveAndFlush(account);

        // when
        account.setBalance(new BigDecimal(200));
        accountRepository.saveAndFlush(account);
        Account retrievedAccountAfterUpdate = accountRepository.findById(account.getAccountId()).orElse(null);


        // then
        assertNotNull(retrievedAccountAfterUpdate);
        assertEquals(new BigDecimal(200), retrievedAccountAfterUpdate.getBalance());
    }

    @Test
    void readAccount_shouldReturnAllData() {
        // given
        Account account = new Account(new BigDecimal(100));
        accountRepository.saveAndFlush(account);

        // when
        Account retrievedAccount = accountRepository.findById(account.getAccountId()).orElse(null);

        // then
        assertNotNull(retrievedAccount);
        assertEquals(new BigDecimal(100), retrievedAccount.getBalance());
    }
}