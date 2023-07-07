package com.lending.application.domain;

import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThrows(DataIntegrityViolationException.class, () -> clientRepository.saveAndFlush(client));
    }

    @Test
    void createClient_shouldReturn1() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        clientRepository.saveAndFlush(client);

        // when & then
        assertEquals(1,clientRepository.count());
    }

    @Test
    void deleteClient_ShouldReturn0(){
        //given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        clientRepository.saveAndFlush(client);

        // when
        clientRepository.delete(client);

        // then
        assertEquals(0,clientRepository.count());
    }

    @Test
    void updateClient_shouldReturnUpdatedData() {
        // given
        Client client = new Client(
                "Client",
                "Last name",
                "Address",
                "666-666-666",
                "test@gmail.com"
        );

        clientRepository.saveAndFlush(client);

        // when
        client.setName("Updated name");
        client.setLastName("Updated last name");
        client.setAddress("Updated address");
        client.setPhoneNumber("111-111-111");
        client.setEmailAddress("updated@gmail.com");
        clientRepository.saveAndFlush(client);

        // when & then
        assertEquals("Updated name", clientRepository
                .findAll()
                .get(0)
                .getName());
        assertEquals("Updated last name", clientRepository
                .findAll()
                .get(0)
                .getLastName());
        assertEquals("Updated address", clientRepository
                .findAll()
                .get(0)
                .getAddress());
        assertEquals("111-111-111", clientRepository
                .findAll()
                .get(0)
                .getPhoneNumber());
        assertEquals("updated@gmail.com", clientRepository
                .findAll()
                .get(0)
                .getEmailAddress());
    }

    @Test
    void readClient_shouldReturnAllData() {
        // given
        Client client = new Client(
                "Client",
                "Last name",
                "Address",
                "test@gmail.com",
                "666-666-666"
        );

        clientRepository.saveAndFlush(client);

        // when & then
        assertEquals("Client", clientRepository
                .findAll()
                .get(0)
                .getName());
        assertEquals("Last name", clientRepository
                .findAll()
                .get(0)
                .getLastName());
        assertEquals("Address", clientRepository
                .findAll()
                .get(0)
                .getAddress());
        assertEquals("666-666-666", clientRepository
                .findAll()
                .get(0)
                .getPhoneNumber());
        assertEquals("test@gmail.com", clientRepository
                .findAll()
                .get(0)
                .getEmailAddress());
    }
}