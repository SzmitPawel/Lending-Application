package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.mapper.ClientMapper;
import com.lending.application.repository.ClientRepository;
import com.lending.application.exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;

    @Test
    void testGetClientById_ClientNotFoundException() {
        // given
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void testDeleteClientById_ClientNotFoundException() {
        // given
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(1L));
    }

    @Test
    void testCreateClient() {
        // given
        ClientDto clientDto = new ClientDto();
        Client client = new Client();

        when(clientMapper.mapToClient(clientDto)).thenReturn(client);
        when(clientRepository.saveAndFlush(client)).thenReturn(client);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        // when
        ClientDto retrievedClient = clientService.createClient(clientDto);

        // then
        verify(clientMapper,times(1)).mapToClient(any(ClientDto.class));
        verify(clientRepository,times(1)).saveAndFlush(any(Client.class));
        verify(clientMapper,times(1)).mapToClientDto(any(Client.class));

        assertNotNull(retrievedClient);
    }

    @Test
    void testGetClientById() throws ClientNotFoundException {
        // given
        Client client = new Client();
        ClientDto clientDto = new ClientDto();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        // when
        ClientDto retrievedClient = clientService.getClientById(1L);

        // then
        verify(clientRepository,times(1)).findById(any());
        verify(clientMapper,times(1)).mapToClientDto(any(Client.class));

        assertNotNull(retrievedClient);
    }

    @Test
    void testGetAllClients_shouldReturnListOfClientsDto() {
        // given
        Client client1 = new Client();
        Client client2 = new Client();
        List<Client> clientList = List.of(client1,client2);

        ClientDto clientDto1 = new ClientDto();
        ClientDto clientDto2 = new ClientDto();
        List<ClientDto> clientDtoList = List.of(clientDto1,clientDto2);

        when(clientRepository.findAll()).thenReturn(clientList);
        when(clientMapper.mapToClientDtoList(clientList)).thenReturn(clientDtoList);

        // when
        List<ClientDto> retrievedClientList = clientService.getAllClients();

        // then
        verify(clientRepository,times(1)).findAll();
        verify(clientMapper,times(1)).mapToClientDtoList(anyList());

        assertNotNull(retrievedClientList);
    }

    @Test
    void testUpdateClient() throws ClientNotFoundException {
        // given
        ClientDto clientDto = new ClientDto();
        clientDto.setClientId(1L);
        Client client = new Client();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.saveAndFlush(client)).thenReturn(client);
        when(clientMapper.mapToClientDto(client)).thenReturn(clientDto);

        // when
        ClientDto updateClientClient = clientService.updateClient(clientDto);

        // then
        verify(clientRepository,times(1)).findById(any());
        verify(clientRepository,times(1)).saveAndFlush(any(Client.class));
        verify(clientMapper,times(1)).mapToClientDto(any(Client.class));

        assertNotNull(updateClientClient);
    }

    @Test
    void deleteClientById() throws ClientNotFoundException {
        // given
        Client client = new Client();

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // when
        clientService.deleteClientById(1L);

        // then
        verify(clientRepository,times(1)).deleteById(1L);
    }
}