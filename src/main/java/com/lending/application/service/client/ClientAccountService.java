package com.lending.application.service.client;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.mapper.ClientMapper;
import com.lending.application.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClientAccountService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientDto createClient(final ClientDto clientDto) {
        Client client = clientMapper.mapToClient(clientDto);
        Account account = new Account(new BigDecimal(0));

        client.setAccount(account);

        Client retrievedClient = clientRepository.saveAndFlush(client);

        return clientMapper.mapToClientDto(retrievedClient);
    }
}
