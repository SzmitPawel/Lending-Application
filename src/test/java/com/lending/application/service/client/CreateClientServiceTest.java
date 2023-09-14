package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClientServiceTest {
    @InjectMocks
    private CreateClientService createClientService;
    @Mock
    private ClientRepository clientRepository;

    @Test
    void createClient_succeed() {
        // given
        Client client = new Client();

        when(clientRepository.saveAndFlush(client)).thenReturn(client);

        // when
        Client createdClient = createClientService.createClient(client);

        // then
        verify(clientRepository,times(1)).saveAndFlush(any(Client.class));

        assertNotNull(createdClient);
    }
}