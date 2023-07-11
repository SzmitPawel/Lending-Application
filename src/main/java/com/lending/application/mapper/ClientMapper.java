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
        return new Client(
                clientDto.getClientID(),
                clientDto.getName(),
                clientDto.getLastName(),
                clientDto.getAddress(),
                clientDto.getEmailAddress(),
                clientDto.getPhoneNumber()
        );
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