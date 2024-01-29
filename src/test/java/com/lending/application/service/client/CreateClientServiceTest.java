package com.lending.application.service.client;

import com.lending.application.domain.client.Client;
import com.lending.application.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Transactional
class CreateClientServiceTest {
    @Autowired
    private CreateClientService createClientService;
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
    void createClient_succeed() {
        // given
        Client client = prepareClient("John", "Doe");

        // when
        Client createdClient = createClientService.createClient(client);

        // then
        assertNotNull(createdClient);
        assertEquals(client.getName(),createdClient.getName());
        assertEquals(client.getLastName(),createdClient.getLastName());
        assertNotNull(createdClient.getAccount());
        assertEquals(BigDecimal.ZERO,createdClient.getAccount().getBalance());
    }
}