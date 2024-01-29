package com.lending.application.domain;

import com.lending.application.domain.client.Client;
import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

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
        assertThrows(DataIntegrityViolationException.class, () -> clientRepository.saveAndFlush(client));
    }

    @Test
    void createClient_shouldReturnClient() {
        // given
        Client client = new Client(
                "Client",
                "Last name",
                null,
                null,
                null
        );
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertEquals(1,clientRepository.count());
    }

    @Test
    void deleteClient_ShouldReturn0(){
        //given
        Client client = new Client(
                "Client",
                "Last name",
                null,
                null,
                null
        );
        clientRepository.saveAndFlush(client);

        // when
        clientRepository.delete(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNull(retrievedClientAfterDelete);
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

        Client retrievedClientAfterUpdate = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterUpdate);
        assertEquals("Updated name", retrievedClientAfterUpdate.getName());
        assertEquals("Updated last name", retrievedClientAfterUpdate.getLastName());
        assertEquals("Updated address", retrievedClientAfterUpdate.getAddress());
        assertEquals("111-111-111", retrievedClientAfterUpdate.getPhoneNumber());
        assertEquals("updated@gmail.com", retrievedClientAfterUpdate.getEmailAddress());
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

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // when & then
        assertNotNull(retrievedClient);
        assertEquals("Client", retrievedClient.getName());
        assertEquals("Last name", retrievedClient.getLastName());
        assertEquals("Address", retrievedClient.getAddress());
        assertEquals("666-666-666", retrievedClient.getPhoneNumber());
        assertEquals("test@gmail.com", retrievedClient.getEmailAddress());
    }
}