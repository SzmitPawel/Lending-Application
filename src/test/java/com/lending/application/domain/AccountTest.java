package com.lending.application.domain;

import com.lending.application.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void createAccount_shouldReturn1() {
        // given
        Account account = new Account();

        accountRepository.saveAndFlush(account);

        // when & then
        assertEquals(1,accountRepository.count());
    }

    @Test
    void deleteOneAccount_shouldReturn0() {
        //given
        Account account = new Account();

        accountRepository.saveAndFlush(account);

        // when
        accountRepository.delete(account);

        // then
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

        // then
        assertEquals(new BigDecimal(200),accountRepository
                .findAll()
                .get(0)
                .getBalance());
    }
    @Test
    void readAccount_shouldReturnAllData() {
        // given
        Account account = new Account(new BigDecimal(100));

        accountRepository.saveAndFlush(account);

        // when & then
        assertEquals(new BigDecimal(100), accountRepository
                .findAll()
                .get(0)
                .getBalance());
    }
}