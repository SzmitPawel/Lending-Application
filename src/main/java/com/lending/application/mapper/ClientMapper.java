package com.lending.application.mapper;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapper {
    public ClientDto mapToClientDto(final Client client) {
        return new ClientDto(
                client.getClientID(),
                client.getName(),
                client.getLastName(),
                client.getAddress(),
                client.getEmailAddress(),
                client.getPhoneNumber()
        );
    }

    public Client mapToClient(final ClientDto clientDto) {
        Client client = new Client();
        client.setClientID(clientDto.getClientId());
        client.setName(clientDto.getName());
        client.setLastName(clientDto.getLastName());
        client.setAddress(clientDto.getAddress());
        client.setEmailAddress(clientDto.getEmailAddress());
        client.setPhoneNumber(clientDto.getPhoneNumber());

        return client;
    }

    public List<ClientDto> mapToClientDtoList(final List<Client> clientList) {
        return clientList.stream()
                .map(this::mapToClientDto)
                .collect(Collectors.toList());
    }

    public List<Client> mapToClientList(final List<ClientDto> clientDtoList) {
        return clientDtoList.stream()
                .map(this::mapToClient)
                .collect(Collectors.toList());
    }
}