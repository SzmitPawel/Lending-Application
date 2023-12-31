package com.lending.application.service.client;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateClientService {
    private final ClientRepository clientRepository;

    public Client createClient(final Client client) {
        Client createdClient = client;
        Account account = new Account(BigDecimal.ZERO);
        createdClient.setAccount(account);

        return clientRepository.saveAndFlush(createdClient);
    }
}
