package com.lending.application.service.client;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.repository.ClientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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