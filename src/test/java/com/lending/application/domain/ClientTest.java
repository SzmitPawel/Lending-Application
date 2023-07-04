package com.lending.application.domain;

import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;

    @Test
    void createClientWithoutNameAndLastName_shouldReturnDataIntegrityViolationException() {
        // given
        Client client = new Client();

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> clientRepository.save(client));
    }

    @Test
    void deleteClient_ShouldReturn0(){
        //given
        Client client = new Client();
        client.setName("Joe");
        client.setLastName("Dole");

        clientRepository.save(client);
        Long clientId = clientRepository.findAll().get(0).getClientID();

        // when
        clientRepository.deleteById(clientId);

        // then
        assertEquals(0,clientRepository.count());
    }

    @Test
    void createClient_shouldReturn1() {
        // given
        Client client = new Client();
        client.setName("Joe");
        client.setLastName("Dole");

        clientRepository.save(client);

        // when & then
        assertEquals(1,clientRepository.count());
    }

    @Test
    void createMultipleClients_shouldReturn3Clients() {
        // given
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();

        client1.setName("Client 1");
        client2.setName("Client 2");
        client3.setName("Client 3");

        client1.setLastName("Last 1");
        client2.setLastName("Last 2");
        client3.setLastName("Last 3");

        clientRepository.saveAll(Arrays.asList(client1,client2,client3));

        // when & then
        assertEquals(3, clientRepository.count());
    }

    @Test
    void createClientWithAccount_shouldReturn1ClientAnd1Account() {
        // given
        Client client = new Client();
        Account account = new Account();

        client.setName("Client");
        client.setLastName("Last");

        // when
        client.setAccount(account);
        clientRepository.save(client);

        // then
        assertEquals(1,clientRepository.count());
        assertEquals(1,accountRepository.count());
    }

    @Test
    void createMultipleClientsWithAccounts_shouldCheckBalances() {
        // given
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();

        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();

        client1.setName("Client 1");
        client2.setName("Client 2");
        client3.setName("Client 3");

        client1.setLastName("Last 1");
        client2.setLastName("Last 2");
        client3.setLastName("Last 3");

        account1.setBalance(new BigDecimal(100));
        account2.setBalance(new BigDecimal(200));
        account3.setBalance(new BigDecimal(300));

        // when
        client1.setAccount(account1);
        client2.setAccount(account2);
        client3.setAccount(account3);

        clientRepository.saveAll(Arrays.asList(client1,client2,client3));

        // then
        assertEquals(new BigDecimal(100), clientRepository.findAll().get(0).getAccount().getBalance());
        assertEquals(new BigDecimal(200), clientRepository.findAll().get(1).getAccount().getBalance());
        assertEquals(new BigDecimal(300), clientRepository.findAll().get(2).getAccount().getBalance());
    }

    @Test
    void deleteClientWithAccount_AfterDeleteClientAccountShouldBeDeletedToo() {
        // given
        Client client = new Client();
        Account account = new Account();

        client.setName("Client");
        client.setLastName("Last");

        client.setAccount(account);
        clientRepository.save(client);

        // when
        Long clientId = clientRepository.findAll().get(0).getClientID();
        clientRepository.deleteById(clientId);

        // then
        assertEquals(0,clientRepository.count());
        assertEquals(0,accountRepository.count());
    }
}