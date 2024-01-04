package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.transaction.TransactionCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositCommandTest {
    @InjectMocks
    private DepositCommand depositCommand;
    @Mock
    private ClientService clientService;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionCommand transactionCommand;

    private Client prepareClient(final BigDecimal accountBalance) {
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        Account account = new Account(accountBalance);
        client.setAccount(account);

        return client;
    }

    @Test
    public void deposit_should_throw_client_not_found_exception_if_client_not_exist()
            throws ClientNotFoundException {

        // given
        Long clientId = 999L;
        BigDecimal depositAmount = BigDecimal.ONE;

        when(clientService.getClientById(clientId)).thenThrow(ClientNotFoundException.class);

        // when & then
        assertThrows(ClientNotFoundException.class, () -> depositCommand.doDeposit(clientId,depositAmount));
    }

    @Test
    public void deposit_should_return_correct_account_balance_if_do_deposit_succeed()
            throws ClientNotFoundException {

        // given
        BigDecimal accountBalance = BigDecimal.TEN;
        Client client = prepareClient(accountBalance);
        BigDecimal depositAmount = BigDecimal.TEN;
        Long clientId = client.getClientId();

        BigDecimal expectedResult = BigDecimal.valueOf(20);

        when(clientService.getClientById(clientId)).thenReturn(client);

        // when
        BigDecimal retrievedAccountBalance = depositCommand.doDeposit(clientId,depositAmount);

        // then
        verify(transactionCommand,times(1))
                .doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,times(1))
                .saveAccount(any(Account.class));

        assertNotNull(retrievedAccountBalance);
        assertEquals(expectedResult,retrievedAccountBalance);
    }
}