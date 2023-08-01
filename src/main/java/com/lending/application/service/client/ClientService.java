package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.mapper.ClientMapper;
import com.lending.application.repository.ClientRepository;
import com.lending.application.exception.ClientNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ClientService {
    ClientRepository clientRepository;
    ClientMapper clientMapper;

    public ClientDto createClient(final ClientDto clientDto) {
        Client retrievedClient = clientRepository.saveAndFlush(clientMapper.mapToClient(clientDto));
        
        return clientMapper.mapToClientDto(retrievedClient);
    }

    public ClientDto getClientById(final Long clientId) throws ClientNotFoundException {
        return clientMapper.mapToClientDto(clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new));
    }

    public List<ClientDto> getAllClients() {
        return clientMapper.mapToClientDtoList(clientRepository.findAll());
    }

    public ClientDto updateClient(final ClientDto clientDto) throws ClientNotFoundException{
        Client retrievedClient = clientRepository.findById(clientDto.getClientId()).orElseThrow(ClientNotFoundException::new);

        retrievedClient.setName(clientDto.getName());
        retrievedClient.setLastName(clientDto.getLastName());
        retrievedClient.setEmailAddress(clientDto.getEmailAddress());
        retrievedClient.setPhoneNumber(clientDto.getPhoneNumber());

        clientRepository.saveAndFlush(retrievedClient);

        return clientMapper.mapToClientDto(retrievedClient);
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        if (clientRepository.findById(clientId).isPresent()) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }
}