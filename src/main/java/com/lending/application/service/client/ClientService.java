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

    public void createClient(final ClientDto clientDto) {
        clientRepository.saveAndFlush(clientMapper.mapToClient(clientDto));
    }

    public ClientDto getClientById(final Long clientId) throws ClientNotFoundException {
        return clientMapper.mapToClientDto(clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new));
    }

    public List<ClientDto> getAllClients() {
        return clientMapper.mapToClientDtoList(clientRepository.findAll());
    }

    public void updateClient(final ClientDto clientDto) throws ClientNotFoundException{
        Client client = clientRepository.findById(clientDto.getClientID()).orElseThrow(ClientNotFoundException::new);

        client.setName(clientDto.getName());
        client.setLastName(clientDto.getLastName());
        client.setEmailAddress(clientDto.getEmailAddress());
        client.setPhoneNumber(clientDto.getPhoneNumber());

        clientRepository.saveAndFlush(client);
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        if (clientRepository.findById(clientId).isPresent()) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }
}