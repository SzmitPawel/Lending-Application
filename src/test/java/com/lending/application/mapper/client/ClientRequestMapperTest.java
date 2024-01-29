package com.lending.application.mapper.client;

import client.ClientRequestMapperImpl;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.client.ClientRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ClientRequestMapperImpl.class})
public class ClientRequestMapperTest {
    @Autowired
    private ClientRequestMapper clientRequestMapper;

    private ClientRequestDTO prepareClientDTO() {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setName("John");
        clientRequestDTO.setLastName("Doe");
        clientRequestDTO.setAddress("123 Main St, Apt 4, New York, NY 10001");
        clientRequestDTO.setEmailAddress("example@mail.com");
        clientRequestDTO.setPhoneNumber("555-555-555");

        return clientRequestDTO;
    }


    @Test
    void map_to_client_should_return_client() {
        // given
        ClientRequestDTO clientRequestDTO = prepareClientDTO();

        // when
        Client retrievedClient = clientRequestMapper.mapToClient(clientRequestDTO);

        // then
        assertNotNull(retrievedClient);
        assertEquals(clientRequestDTO.getName(), retrievedClient.getName());
        assertEquals(clientRequestDTO.getLastName(), retrievedClient.getLastName());
        assertEquals(clientRequestDTO.getAddress(), retrievedClient.getAddress());
        assertEquals(clientRequestDTO.getEmailAddress(), retrievedClient.getEmailAddress());
        assertEquals(clientRequestDTO.getPhoneNumber(), retrievedClient.getPhoneNumber());
    }

    @Test
    void update_client_from_dto_should_updated_client_and_ignore_null_value_1() {
        // given
        Client existingClient = new Client();
        existingClient.setClientId(1L);
        existingClient.setName("John");
        existingClient.setLastName("Doe");
        existingClient.setAddress("123 Main St, Apt 4, New York, NY 10001");
        existingClient.setEmailAddress("example@mail.com");
        existingClient.setPhoneNumber("555-555-555");

        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setName("Dave");

        // when
        clientRequestMapper.updateClientFromDto(requestDTO, existingClient);

        // then
        assertEquals(1L, existingClient.getClientId());
        assertEquals(requestDTO.getName(), existingClient.getName());
        assertEquals("Doe", existingClient.getLastName());
        assertEquals("123 Main St, Apt 4, New York, NY 10001", existingClient.getAddress());
        assertEquals("example@mail.com", existingClient.getEmailAddress());
        assertEquals("555-555-555", existingClient.getPhoneNumber());
    }

    @Test
    void update_client_from_dto_should_updated_client_and_ignore_null_value_2() {
        // given
        Client existingClient = new Client();
        existingClient.setClientId(1L);
        existingClient.setName("John");
        existingClient.setLastName("Doe");
        existingClient.setAddress("123 Main St, Apt 4, New York, NY 10001");
        existingClient.setEmailAddress("example@mail.com");
        existingClient.setPhoneNumber("555-555-555");

        ClientRequestDTO requestDTO = new ClientRequestDTO();
        requestDTO.setName("Dave");
        requestDTO.setPhoneNumber("666-666-666");

        // when
        clientRequestMapper.updateClientFromDto(requestDTO, existingClient);

        // then
        assertEquals(1L, existingClient.getClientId());
        assertEquals(requestDTO.getName(), existingClient.getName());
        assertEquals("Doe", existingClient.getLastName());
        assertEquals("123 Main St, Apt 4, New York, NY 10001", existingClient.getAddress());
        assertEquals("example@mail.com", existingClient.getEmailAddress());
        assertEquals(requestDTO.getPhoneNumber(), existingClient.getPhoneNumber());
    }
}
