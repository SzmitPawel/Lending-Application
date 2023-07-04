package com.lending.application.domain;

import com.lending.application.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void createOneAccount_ShouldReturn1() {
        // given
        Account account = new Account();

        accountRepository.save(account);

        // when & then
        assertEquals(1,accountRepository.count());
    }

    @Test
    void createMultipleAccounts_ShouldReturn3() {
        // given
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();

        accountRepository.saveAll(Arrays.asList(account1,account2,account3));

        // when & then
        assertEquals(3,accountRepository.count());
    }

    @Test
    void createAccountAndAddBalance_ShouldReturn1AccountAndCheckBalance(){
        // given
        Account account = new Account();
        account.setBalance(new BigDecimal(100));

        accountRepository.save(account);

        // when & then

        assertEquals(1, accountRepository.count());
        assertEquals(new BigDecimal(100), accountRepository.findAll().get(0).getBalance());
    }

    @Test
    void createMultipleAccountsAndBalances_ShouldReturnAllAccountsAndBalances() {
        // given
        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();

        account1.setBalance(new BigDecimal(100));
        account2.setBalance(new BigDecimal(200));
        account3.setBalance(new BigDecimal(300));

        accountRepository.saveAll(Arrays.asList(account1,account2,account3));

        // when & then
        assertEquals(3,accountRepository.count());
        assertEquals(new BigDecimal(100), accountRepository.findAll().get(0).getBalance());
        assertEquals(new BigDecimal(200), accountRepository.findAll().get(1).getBalance());
        assertEquals(new BigDecimal(300), accountRepository.findAll().get(2).getBalance());
    }
}