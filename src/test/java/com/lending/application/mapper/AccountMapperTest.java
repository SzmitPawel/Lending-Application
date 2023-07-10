package com.lending.application.mapper;

import com.lending.application.domain.Account;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.domain.dto.AccountDto;
import com.lending.application.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountMapperTest {
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    AccountRepository accountRepository;

    @Test
    void mapToAccountDtoTest() {
        // given
        Account account = new Account(new BigDecimal(1));

        Transaction transaction1 = new Transaction(
                new BigDecimal(1),
                LocalDate.now(), TransactionMethodEnum.DEPOSIT
        );

        transaction1.setPaymentID(1L);

        Transaction transaction2 = new Transaction(
                new BigDecimal(2),
                LocalDate.now(),
                TransactionMethodEnum.WITHDRAWAL
        );

        List<Transaction> transactionList = List.of(transaction1,transaction2);
        account.setTransactionList(transactionList);

        // when
        AccountDto retrievedAccountDto = accountMapper.mapToAccountDto(account);

        // then
        assertEquals(new BigDecimal(1), retrievedAccountDto.getBalance());
        assertEquals(1L, retrievedAccountDto.getTransactionIds().get(0));
        assertEquals(2L, retrievedAccountDto.getTransactionIds().get(1));
    }

    @Test
    void mapToAccountTest() {
        // given
        Account account = new Account(new BigDecimal(1));

        Transaction transaction1 = new Transaction(
                new BigDecimal(1),
                LocalDate.now(), TransactionMethodEnum.DEPOSIT
        );

        transaction1.setPaymentID(1L);

        Transaction transaction2 = new Transaction(
                new BigDecimal(2),
                LocalDate.now(),
                TransactionMethodEnum.WITHDRAWAL
        );

        List<Transaction> transactionList = List.of(transaction1,transaction2);
        account.setTransactionList(transactionList);

        accountRepository.saveAndFlush(account);

        // when

    }
}