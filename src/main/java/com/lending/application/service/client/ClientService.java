package com.lending.application.service.client;

import com.lending.application.domain.Client;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client getClientById(final Long clientId) throws ClientNotFoundException {
        return clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
    }

    public List<Client> getAllClientsList() {
        return clientRepository.findAll();
    }

    public Client saveClient(final Client client) {
        return clientRepository.saveAndFlush(client);
    }

    public void deleteClientById(final Long clientId) throws ClientNotFoundException {
        if (clientRepository.findById(clientId).isPresent()) {
            clientRepository.deleteById(clientId);
        } else {
            throw new ClientNotFoundException();
        }
    }
}