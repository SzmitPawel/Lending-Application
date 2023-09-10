package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.mapper.ClientMapper;
import com.lending.application.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientAccountServiceTest {
    @InjectMocks
    private ClientAccountService clientAccountService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;

    @Test
    void createClient_succeed() {
        // given
        ClientDto clientDto = new ClientDto();
        Client client = new Client();

        when(clientMapper.mapToClient(clientDto)).thenReturn(client);
        when(clientRepository.saveAndFlush(client)).thenReturn(client);

        // when
        clientAccountService.createClient(clientDto);

        // then
        verify(clientMapper,times(1)).mapToClientDto(any(Client.class));
    }
}