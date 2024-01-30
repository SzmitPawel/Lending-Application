package com.lending.application.service.account;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceCommandTest {
    @InjectMocks
    private BalanceCommand balanceCommand;
    @Mock
    private ClientService clientService;

    @Test
    void getBalance_should_throw_client_not_found_exception_if_client_not_exist()
            throws ClientNotFoundException
    {
        // given
        Long clientId = 999L;

        when(clientService.getClientById(clientId)).thenThrow(ClientNotFoundException.class);

        // when & then
        assertThrows(ClientNotFoundException.class, () -> balanceCommand.getBalance(clientId));
    }

    @Test
    void getBalance_should_return_balance_if_get_balance_succeed()
            throws ClientNotFoundException
    {
        // given
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        Account account = new Account(BigDecimal.TEN);
        client.setAccount(account);

        BigDecimal expectedResult = BigDecimal.TEN;


        when(clientService.getClientById(client.getClientId())).thenReturn(client);

        // when
        BigDecimal retrievedAccountBalance = balanceCommand.getBalance(client.getClientId());

        // then
        assertEquals(expectedResult,retrievedAccountBalance);
    }

}