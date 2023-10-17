package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ClientServiceTest {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;

    private Client prepareClient(final String name, final String lastName) {
        Client client = new Client();
        client.setName(name);
        client.setLastName(lastName);
        client.setPhoneNumber("999-999-999");
        client.setAddress("Avenue Street 33A");

        return client;
    }

    @Test
    void testGetClientById_ClientNotFoundException() {
        // given
        Long clientId = 999L;

        // when & then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(clientId));
    }

    @Test
    void testGetClientById_shouldReturnClient() throws ClientNotFoundException {
        // given
        Client client = clientRepository.saveAndFlush(prepareClient("John", "Doe"));

        // when
        Client retrievedClient = clientService.getClientById(client.getClientId());

        // then
        assertNotNull(retrievedClient);
        assertEquals(client.getName(),retrievedClient.getName());
        assertEquals(client.getLastName(),retrievedClient.getLastName());
        assertEquals(client.getPhoneNumber(),retrievedClient.getPhoneNumber());
        assertEquals(client.getAddress(),retrievedClient.getAddress());
    }

    @Test
    void testGetAllClients_shouldReturnListOfClients() {
        // given
        Client client1 = clientRepository.saveAndFlush(prepareClient("John1", "Doe1"));
        Client client2 = clientRepository.saveAndFlush(prepareClient("John2", "Doe2"));

        List<Client> clientList = List.of(client1,client2);

        // when
        List<Client> retrievedClientList = clientService.getAllClientsList();

        // then
        assertNotNull(retrievedClientList);
        assertEquals(clientList.size(),retrievedClientList.size());
        assertEquals(clientList.get(0).getName(),retrievedClientList.get(0).getName());
        assertEquals(clientList.get(0).getLastName(),retrievedClientList.get(0).getLastName());
        assertEquals(clientList.get(0).getPhoneNumber(),retrievedClientList.get(0).getPhoneNumber());
        assertEquals(clientList.get(0).getAddress(),retrievedClientList.get(0).getAddress());

        assertEquals(clientList.get(1).getName(),retrievedClientList.get(1).getName());
        assertEquals(clientList.get(1).getLastName(),retrievedClientList.get(1).getLastName());
        assertEquals(clientList.get(1).getPhoneNumber(),retrievedClientList.get(1).getPhoneNumber());
        assertEquals(clientList.get(1).getAddress(),retrievedClientList.get(1).getAddress());
    }

    @Test
    void testSaveClient() {
        // given
        Client client = clientRepository.saveAndFlush(prepareClient("John", "Doe"));

        // when
        Client retrievedClient = clientService.saveClient(client);

        // then
        assertNotNull(retrievedClient);
        assertEquals(client.getName(),retrievedClient.getName());
        assertEquals(client.getLastName(),retrievedClient.getLastName());
        assertEquals(client.getPhoneNumber(),retrievedClient.getPhoneNumber());
        assertEquals(client.getAddress(),retrievedClient.getAddress());
    }

    @Test
    void testDeleteClientById_ClientNotFoundException() {
        // given
        Long clientId = 999L;

        // when & then
        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(clientId));
    }

    @Test
    void deleteClientById() throws ClientNotFoundException {
        // given
        Client client = clientRepository.saveAndFlush(prepareClient("John","Doe"));

        // when
        clientService.deleteClientById(client.getClientId());

        // then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(client.getClientId()));
    }
}