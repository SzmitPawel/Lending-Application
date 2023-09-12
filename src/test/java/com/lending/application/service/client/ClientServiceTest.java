package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;
    @Mock
    private ClientRepository clientRepository;

    @Test
    void testGetClientById_ClientNotFoundException() {
        // given
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void testGetClientById() throws ClientNotFoundException {
        // given
        Client client = new Client();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        // when
        Client retrievedClient = clientService.getClientById(1L);

        // then
        verify(clientRepository,times(1)).findById(any());

        assertNotNull(retrievedClient);
    }

    @Test
    void testGetAllClients_shouldReturnListOfClientsDto() {
        // given
        Client client1 = new Client();
        Client client2 = new Client();
        List<Client> clientList = List.of(client1,client2);

        when(clientRepository.findAll()).thenReturn(clientList);

        // when
        List<Client> retrievedClientList = clientService.getAllClientsList();

        // then
        verify(clientRepository,times(1)).findAll();

        assertNotNull(retrievedClientList);
    }

    @Test
    void testUpdateClient() throws ClientNotFoundException {
        // given
        Client client = new Client();
        ClientDto clientDto = new ClientDto();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(clientRepository.saveAndFlush(client)).thenReturn(client);

        // when
        Client updateClientClient = clientService.updateClient(clientDto);

        // then
        verify(clientRepository,times(1)).findById(any());
        verify(clientRepository,times(1)).saveAndFlush(any(Client.class));

        assertNotNull(updateClientClient);
    }

    @Test
    void testDeleteClientById_ClientNotFoundException() {
        // given
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(1L));
    }

    @Test
    void deleteClientById() throws ClientNotFoundException {
        // given
        Client client = new Client();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        // when
        clientService.deleteClientById(1L);

        // then
        verify(clientRepository,times(1)).deleteById(1L);
    }
}