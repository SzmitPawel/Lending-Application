package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.ClientMapper;
import com.lending.application.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientDto createClient(final ClientDto clientDto) {
        Client client = clientMapper.mapToClient(clientDto);
        Client retrievedClient = clientRepository.saveAndFlush(client);
        
        return clientMapper.mapToClientDto(retrievedClient);
    }

    public ClientDto getClientById(final Long clientId) throws ClientNotFoundException {
        Client retrievedClient = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);

        return clientMapper.mapToClientDto(retrievedClient);
    }

    public List<ClientDto> getAllClients() {
        List<Client> retrievedClientList = clientRepository.findAll();
        return clientMapper.mapToClientDtoList(retrievedClientList);
    }

    public ClientDto updateClient(final ClientDto clientDto) throws ClientNotFoundException{
        Client retrievedClient = clientRepository.findById(clientDto.getClientId()).orElseThrow(ClientNotFoundException::new);

        retrievedClient.setName(clientDto.getName());
        retrievedClient.setLastName(clientDto.getLastName());
        retrievedClient.setEmailAddress(clientDto.getEmailAddress());
        retrievedClient.setPhoneNumber(clientDto.getPhoneNumber());

        Client updatedClient  = clientRepository.saveAndFlush(retrievedClient);

        return clientMapper.mapToClientDto(updatedClient);
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        if (clientRepository.findById(clientId).isPresent()) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }
}