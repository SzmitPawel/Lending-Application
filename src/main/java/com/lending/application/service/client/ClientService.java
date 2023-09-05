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

    public Client createClient(final ClientDto clientDto) {
        Client client = clientMapper.mapToClient(clientDto);
        Client retrievedClient = clientRepository.saveAndFlush(client);
        
        return retrievedClient;
    }

    public Client getClientById(final Long clientId) throws ClientNotFoundException {

        return clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client updateClient(final ClientDto clientDto) throws ClientNotFoundException{
        Client retrievedClient = clientRepository.findById(clientDto.getClientId()).orElseThrow(ClientNotFoundException::new);

        retrievedClient.setName(clientDto.getName());
        retrievedClient.setLastName(clientDto.getLastName());
        retrievedClient.setEmailAddress(clientDto.getEmailAddress());
        retrievedClient.setPhoneNumber(clientDto.getPhoneNumber());

        return clientRepository.saveAndFlush(retrievedClient);
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        if (clientRepository.findById(clientId).isPresent()) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }
}