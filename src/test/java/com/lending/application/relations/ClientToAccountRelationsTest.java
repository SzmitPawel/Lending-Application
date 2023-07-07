package com.lending.application.relations;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.ClientRepository;
import com.lending.application.repository.CreditRatingRepository;
import com.lending.application.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ClientToAccountRelationsTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CreditRatingRepository creditRatingRepository;
    @Autowired
    LoanRepository loanRepository;

    @Test
    void createClientWithAccount_shouldReturn1ClientAnd1Account() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Account account = new Account();

        client.setAccount(account);

        clientRepository.save(client);

        // when & then
        assertEquals(1, clientRepository.count());
        assertEquals(1, accountRepository.count());
    }

    @Test
    void deleteAccount_shouldReturn1ClientAnd0Accounts() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Account account = new Account();
        account.setBalance(new BigDecimal(1));

        client.setAccount(account);
        clientRepository.saveAndFlush(client);

        // when
        client = clientRepository.findAll().get(0);
        account = client.getAccount();
        client.setAccount(null);
        accountRepository.delete(account);
        clientRepository.saveAndFlush(client);

        // then
        assertEquals(1, clientRepository.count());
        assertEquals(0, accountRepository.count());
    }

    @Test
    void updateAccount_shouldReturnUpdatedData() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Account account = new Account();
        account.setBalance(new BigDecimal(1));

        client.setAccount(account);
        clientRepository.save(client);

        // when
        account.setBalance(new BigDecimal(200));
        accountRepository.save(account);

        // then
        assertEquals(new BigDecimal(200), clientRepository.findAll()
                .get(0)
                .getAccount()
                .getBalance());
    }

    @Test
    void readAccount_shouldReturnAllData() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Account account = new Account();
        account.setBalance(new BigDecimal(1));

        client.setAccount(account);
        clientRepository.save(client);

        // when & then
        assertEquals(new BigDecimal(1), clientRepository.findAll()
                .get(0)
                .getAccount()
                .getBalance());
    }
}