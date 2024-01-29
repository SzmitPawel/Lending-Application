package com.lending.application.mapper.client;

import client.ClientResponseMapperImpl;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.client.ClientResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ClientResponseMapperImpl.class})
class ClientResponseMapperTest {
    @Autowired
    private ClientResponseMapperImpl clientResponseMapper;

    private Client prepareClient(Long id) {

        Client client = new Client();
        client.setClientId(id);
        client.setName("John");
        client.setLastName("Doe");
        client.setAddress("123 Main St, Apt 4, New York, NY 10001");
        client.setEmailAddress("example@mail.com");
        client.setPhoneNumber("555-555-555");

        return client;
    }


    @Test
    void map_to_client_dto_should_return_client_dto() {
        // given
        Client client = prepareClient(1L);

        // when
        ClientResponseDTO retrievedClientResponseDTO = clientResponseMapper.mapToClientDto(client);

        // then
        assertNotNull(retrievedClientResponseDTO);
        assertEquals(client.getClientId(),retrievedClientResponseDTO.getClientId());
        assertEquals(client.getName(),retrievedClientResponseDTO.getName());
        assertEquals(client.getLastName(),retrievedClientResponseDTO.getLastName());
        assertEquals(client.getAddress(),retrievedClientResponseDTO.getAddress());
        assertEquals(client.getEmailAddress(),retrievedClientResponseDTO.getEmailAddress());
        assertEquals(client.getPhoneNumber(),retrievedClientResponseDTO.getPhoneNumber());
    }


    @Test
    void map_to_client_dto_list_should_return_client_dto_list() {
        // given
        Client client1 = prepareClient(1L);
        Client client2 = prepareClient(2L);

        List<Client> clientList = List.of(client1,client2);

        // when
        List<ClientResponseDTO> retrievedClientResponseDTOList = clientResponseMapper.mapToClientDtoList(clientList);

        // then
        assertNotNull(retrievedClientResponseDTOList);
        assertEquals(2, retrievedClientResponseDTOList.size());
        assertEquals(client1.getClientId(),retrievedClientResponseDTOList.get(0).getClientId());
        assertEquals(client2.getClientId(),retrievedClientResponseDTOList.get(1).getClientId());
    }
}