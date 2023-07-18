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
    ClientService clientService;
    @Mock
    ClientRepository clientRepository;
    @Mock
    ClientMapper clientMapper;

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
        ClientDto clientDto = new ClientDto(
                1L,
                "Client",
                "Client last name",
                null,
                null,
                null);

        Client client = new Client();

        when(clientMapper.mapToClient(clientDto)).thenReturn(client);

        // when
        clientService.createClient(clientDto);

        // then
        verify(clientRepository, times(1)).saveAndFlush(client);
    }

    @Test
    void testGetClientById() throws ClientNotFoundException {
        // given
        Client client = new Client();
        client.setClientID(1L);
        client.setName("Client");
        client.setLastName("Client last name");

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(clientMapper.mapToClientDto(client)).thenCallRealMethod();

        // when
        ClientDto retrievedClientDto = clientService.getClientById(1L);

        // then
        verify(clientRepository, times(1)).findById(1L);
        verify(clientMapper,times(1)).mapToClientDto(client);
        assertEquals("Client",retrievedClientDto.getName());
        assertEquals("Client last name", retrievedClientDto.getLastName());
    }

    @Test
    void testGetAllClients_shouldReturnListOfClientsDto() {
        // given
        Client client1 = new Client();
        client1.setClientID(1L);
        client1.setName("Client");
        client1.setLastName("Client last name");

        Client client2 = new Client();
        client2.setClientID(2L);
        client2.setName("Client 2");
        client2.setLastName("Client 2 last name");

        List<Client> clientList = List.of(client1,client2);

        when(clientMapper.mapToClientDtoList(clientList)).thenCallRealMethod();
        when(clientRepository.findAll()).thenReturn(clientList);

        // when
        List<ClientDto> retrievedClientDtoList = clientService.getAllClients();

        // then
        assertEquals(clientList.size(), retrievedClientDtoList.size());
        verify(clientMapper, times(1)).mapToClientDtoList(clientList);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testUpdateClient() throws ClientNotFoundException {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "Client",
                "Client last name",
                null,
                null,
                null);

        Client client = new Client();

        when(clientRepository.findById(any())).thenReturn(Optional.of(client));

        // when
        clientService.updateClient(clientDto);

        // then
        verify(clientRepository, times(1)).findById(1L);
        assertEquals("Client", client.getName());
        assertEquals("Client last name", client.getLastName());
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