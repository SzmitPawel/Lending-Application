package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class BalanceCommand {
    private final ClientService clientService;

    public BigDecimal getBalance(final Long clientId)
            throws ClientNotFoundException
    {
        Client client = clientService.getClientById(clientId);
        Account account = client.getAccount();

        return account.getBalance();
    }
}
