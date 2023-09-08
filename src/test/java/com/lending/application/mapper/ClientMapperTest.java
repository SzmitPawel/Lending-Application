package com.lending.application.mapper;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClientMapperTest {
    @Autowired
    ClientMapper clientMapper;

    @Test
    void testMapToClientDto() {
        // given
        Client client = new Client(
                "Client",
                "Last name",
                "Address",
                "test@gmail.com",
                "666-666-666"
        );
        client.setClientId(1L);

        // when
        ClientDto retrievedClientDto = clientMapper.mapToClientDto(client);

        // then
        assertEquals(1L, retrievedClientDto.getClientId());
        assertEquals("Client", retrievedClientDto.getName());
        assertEquals("Last name", retrievedClientDto.getLastName());
        assertEquals("Address", retrievedClientDto.getAddress());
        assertEquals("test@gmail.com", retrievedClientDto.getEmailAddress());
        assertEquals("666-666-666", retrievedClientDto.getPhoneNumber());
    }

    @Test
    void testMapToClient() {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "Client",
                "Last name",
                "Address",
                "test@gmail.com",
                "666-666-666"
        );

        // when
        Client retrievedClient = clientMapper.mapToClient(clientDto);

        // then
        assertEquals(1L, retrievedClient.getClientId());
        assertEquals("Client", retrievedClient.getName());
        assertEquals("Last name", retrievedClient.getLastName());
        assertEquals("Address", retrievedClient.getAddress());
        assertEquals("test@gmail.com", retrievedClient.getEmailAddress());
        assertEquals("666-666-666", retrievedClient.getPhoneNumber());
    }

    @Test
    void testMapToClientDtoList() {
        // given
        Client client1 = new Client(
                "Client 1",
                "Last name 1",
                "Address 1",
                "test1@gmail.com",
                "666-666-666"
        );
        client1.setClientId(1L);
        Client client2 = new Client(
                "Client 2",
                "Last name 2",
                "Address 2",
                "test2@gmail.com",
                "777-777-777"
        );
        client2.setClientId(2L);
        List<Client> clientList = List.of(client1,client2);

        // when
        List<ClientDto> retrievedClientDtoList = clientMapper.mapToClientDtoList(clientList);

        // then
        assertEquals(2, retrievedClientDtoList.size());
        assertEquals(1L, retrievedClientDtoList.get(0).getClientId());
        assertEquals("Client 1", retrievedClientDtoList.get(0).getName());
        assertEquals("Last name 1", retrievedClientDtoList.get(0).getLastName());
        assertEquals("Address 1", retrievedClientDtoList.get(0).getAddress());
        assertEquals("test1@gmail.com", retrievedClientDtoList.get(0).getEmailAddress());
        assertEquals("666-666-666", retrievedClientDtoList.get(0).getPhoneNumber());
        assertEquals(2L, retrievedClientDtoList.get(1).getClientId());
        assertEquals("Client 2", retrievedClientDtoList.get(1).getName());
        assertEquals("Last name 2", retrievedClientDtoList.get(1).getLastName());
        assertEquals("Address 2", retrievedClientDtoList.get(1).getAddress());
        assertEquals("test2@gmail.com", retrievedClientDtoList.get(1).getEmailAddress());
        assertEquals("777-777-777", retrievedClientDtoList.get(1).getPhoneNumber());
    }

    @Test
    void testMapToClientList() {
        // given
        ClientDto clientDto1 = new ClientDto(
                1L,
                "Client 1",
                "Last name 1",
                "Address 1",
                "test1@gmail.com",
                "666-666-666"
        );
        ClientDto clientDto2 = new ClientDto(
                2L,
                "Client 2",
                "Last name 2",
                "Address 2",
                "test2@gmail.com",
                "777-777-777"
        );
        List<ClientDto> clientDtoList = List.of(clientDto1,clientDto2);

        // when
        List<Client> retrievedClientList = clientMapper.mapToClientList(clientDtoList);

        // then
        assertEquals(2, retrievedClientList.size());
        assertEquals(1L, retrievedClientList.get(0).getClientId());
        assertEquals("Client 1", retrievedClientList.get(0).getName());
        assertEquals("Last name 1", retrievedClientList.get(0).getLastName());
        assertEquals("Address 1", retrievedClientList.get(0).getAddress());
        assertEquals("test1@gmail.com", retrievedClientList.get(0).getEmailAddress());
        assertEquals("666-666-666", retrievedClientList.get(0).getPhoneNumber());
        assertEquals(2L, retrievedClientList.get(1).getClientId());
        assertEquals("Client 2", retrievedClientList.get(1).getName());
        assertEquals("Last name 2", retrievedClientList.get(1).getLastName());
        assertEquals("Address 2", retrievedClientList.get(1).getAddress());
        assertEquals("test2@gmail.com", retrievedClientList.get(1).getEmailAddress());
        assertEquals("777-777-777", retrievedClientList.get(1).getPhoneNumber());
    }
}