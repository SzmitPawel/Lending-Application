package com.lending.application.facade;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.client.ClientRequestDTO;
import com.lending.application.domain.client.ClientResponseDTO;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.client.ClientRequestMapper;
import com.lending.application.mapper.client.ClientResponseMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.client.CreateClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceFacade {
    private final ClientService clientService;
    private final ClientRequestMapper clientRequestMapper;
    private final ClientResponseMapper clientResponseMapper;
    private final CreateClientService createClientService;

    public ClientResponseDTO createClient(final ClientRequestDTO clientRequestDTO) {
        Client client = clientRequestMapper.mapToClient(clientRequestDTO);

        return clientResponseMapper.mapToClientDto(createClientService.createClient(client));
    }

    public ClientResponseDTO getClientById(final Long clientId)
            throws ClientNotFoundException {
        return clientResponseMapper.mapToClientDto(clientService.getClientById(clientId));
    }

    public List<ClientResponseDTO> getAllClients() {
        return clientResponseMapper.mapToClientDtoList(clientService.getAllClientsList());
    }

    public ClientResponseDTO updateClient(final Long clientId, final ClientRequestDTO clientRequestDTO)
            throws ClientNotFoundException {
        Client existingClient = clientService.getClientById(clientId);
        clientRequestMapper.updateClientFromDto(clientRequestDTO, existingClient);

        return clientResponseMapper.mapToClientDto(clientService.saveClient(existingClient));
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        clientService.deleteClientById(clientId);
    }
}