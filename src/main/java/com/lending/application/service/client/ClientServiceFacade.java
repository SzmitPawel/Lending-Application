package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceFacade {
    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final CreateClientService createClientService;

    public ClientDto createClient(final ClientDto clientDto) {
        Client client = clientMapper.mapToClient(clientDto);
        return clientMapper.mapToClientDto(createClientService.createClient(client));
    }

    public ClientDto getClientById(final Long clientId) throws ClientNotFoundException {
        return clientMapper.mapToClientDto(clientService.getClientById(clientId));
    }

    public List<ClientDto> getAllClients() {
        return clientMapper.mapToClientDtoList(clientService.getAllClientsList());
    }

    public ClientDto updateClient(final ClientDto clientDto) throws ClientNotFoundException {
        return clientMapper.mapToClientDto(clientService.updateClient(clientDto));
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        clientService.deleteClientById(clientId);
    }
}
