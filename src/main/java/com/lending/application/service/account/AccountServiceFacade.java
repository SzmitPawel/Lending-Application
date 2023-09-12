package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceFacade {
    private final AccountService accountService;
    private final ClientService clientService;

    public BigDecimal deposit(final Long clientId, final BigDecimal amount) throws ClientNotFoundException, AccountNotFoundException {
        Client client = clientService.getClientById(clientId);
        Account account = client.getAccount();
        account.getBalance().add(amount);

        return client.getAccount().getBalance();
    }

    public void withdraw(final Long clientId, final BigDecimal amount) {

    }

    public BigDecimal getBalance(final Long clientId) throws ClientNotFoundException, AccountNotFoundException {
        Client client = clientService.getClientById(clientId);
        Account account = accountService.getAccountById(client.getAccount().getAccountId());

        return account.getBalance();
    }
}
