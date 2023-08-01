package com.lending.application.facade;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.mapper.ClientMapper;
import com.lending.application.repository.ClientRepository;
import com.lending.application.service.client.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ClientServiceFacade {
    ClientService clientService;
    ClientRepository clientRepository;
    ClientMapper clientMapper;

    public ClientDto CreateNewClient(final ClientDto clientDto) {
        Client createdClient = clientMapper.mapToClient(clientService.createClient(clientDto));

        // assign account to client and save client to database
        Account account = new Account(new BigDecimal(0));
        createdClient.setAccount(account);

        return clientMapper.mapToClientDto(clientRepository.saveAndFlush(createdClient));
    }
}
