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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        clientDto.setName("John");
        clientDto.setLastName("Doe");

        Client client = new Client();
        client.setClientId(1L);
        client.setName(clientDto.getName());
        client.setLastName(client.getLastName());

        when(clientMapper.mapToClient(clientDto)).thenReturn(client);
        when(clientRepository.saveAndFlush(any(Client.class))).thenReturn(client);

        // when
        Client retrievedClient = clientService.createClient(clientDto);

        // then
        verify(clientMapper,times(1)).mapToClient(clientDto);
        verify(clientRepository,times(1)).saveAndFlush(client);

        assertEquals(client.getClientId(), retrievedClient.getClientId());
        assertEquals(client.getName(), retrievedClient.getName());
        assertEquals(client.getLastName(), retrievedClient.getLastName());
    }

    @Test
    void testGetClientById() throws ClientNotFoundException {
        // given
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // when
        Client retrievedClient = clientService.getClientById(1L);

        // then
        verify(clientRepository, times(1)).findById(1L);

        assertEquals(client.getName(),retrievedClient.getName());
        assertEquals(client.getLastName(), retrievedClient.getLastName());
    }

    @Test
    void testGetAllClients_shouldReturnListOfClientsDto() {
        // given
        Client client1 = new Client();
        client1.setClientId(1L);
        client1.setName("John 1");
        client1.setLastName("Doe 1");

        Client client2 = new Client();
        client2.setClientId(2L);
        client2.setName("John 2");
        client2.setLastName("Doe 2");

        List<Client> clientList = List.of(client1,client2);

        when(clientRepository.findAll()).thenReturn(clientList);

        // when
        List<Client> retrievedClientList = clientService.getAllClients();

        // then
        verify(clientRepository, times(1)).findAll();

        assertEquals(clientList.size(), retrievedClientList.size());
    }

    @Test
    void testUpdateClient() throws ClientNotFoundException {
        // given
        ClientDto clientDto = new ClientDto();
        clientDto.setClientId(1L);
        clientDto.setName("John");
        clientDto.setLastName("Doe");
        clientDto.setPhoneNumber("666-666-666");

        Client retrievedClient = new Client();
        retrievedClient.setClientId(clientDto.getClientId());
        retrievedClient.setName("Old name");
        retrievedClient.setLastName("Old last name");
        retrievedClient.setEmailAddress("old.email@example.pl");
        retrievedClient.setPhoneNumber("987654321");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(retrievedClient));
        when(clientRepository.saveAndFlush(any(Client.class))).thenReturn(retrievedClient);

        // when
        Client updateClientClient = clientService.updateClient(clientDto);

        // then
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository,times(1)).saveAndFlush(any(Client.class));

        assertEquals(clientDto.getName(), updateClientClient.getName());
        assertEquals(clientDto.getLastName(), updateClientClient.getLastName());
        assertEquals(clientDto.getEmailAddress(), updateClientClient.getEmailAddress());
        assertEquals(clientDto.getPhoneNumber(), updateClientClient.getPhoneNumber());
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