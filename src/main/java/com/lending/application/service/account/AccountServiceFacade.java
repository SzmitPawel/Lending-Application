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

    public BigDecimal deposit(final Long clientId, final BigDecimal deposit) throws ClientNotFoundException, AccountNotFoundException {
        Client client = clientService.getClientById(clientId);

        Account account = client.getAccount();
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(deposit);
        account.setBalance(newBalance);

        accountService.saveAccount(account);

        return newBalance;
    }

    public BigDecimal withdraw(final Long clientId, final BigDecimal withdraw) throws ClientNotFoundException {
        Client client = clientService.getClientById(clientId);

        Account account = client.getAccount();
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.subtract(withdraw);
        account.setBalance(newBalance);

        accountService.saveAccount(account);

        return newBalance;
    }

    public BigDecimal getBalance(final Long clientId) throws ClientNotFoundException, AccountNotFoundException {
        Client client = clientService.getClientById(clientId);

        Account account = accountService.getAccountById(client.getAccount().getAccountId());
        BigDecimal currentBalance = account.getBalance();

        return currentBalance;
    }
}
