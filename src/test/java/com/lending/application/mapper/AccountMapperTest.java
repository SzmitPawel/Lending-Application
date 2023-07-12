package com.lending.application.mapper;

import com.lending.application.domain.Account;
import com.lending.application.domain.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {
    @Autowired
    AccountMapper accountMapper;

    @Test
    void testMapToAccountDto() {
        // given
        Account account = new Account(new BigDecimal(100));
        account.setAccountId(1L);

        // when
        AccountDto retrievedAccountDto = accountMapper.mapToDto(account);

        // then
        assertEquals(1L, retrievedAccountDto.getAccountId());
        assertEquals(new BigDecimal(100), retrievedAccountDto.getBalance());
    }

    @Test
    void testMapToAccount() {
        // given
        AccountDto accountDto = new AccountDto(1L, new BigDecimal(100));

        // when
        Account retrievedAccount = accountMapper.mapToAccount(accountDto);

        // then
        assertEquals(1L, retrievedAccount.getAccountId());
        assertEquals(new BigDecimal(100), retrievedAccount.getBalance());
    }
}