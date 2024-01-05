package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.transaction.TransactionCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawCommandTest {
    @InjectMocks
    private WithdrawCommand withdrawCommand;
    @Mock
    private ClientService clientService;
    @Mock
    private TransactionCommand transactionCommand;
    @Mock
    private AccountService accountService;

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
    void doWithdraw_should_throw_client_not_found_exception_if_client_not_exist()
            throws ClientNotFoundException
    {
        // given
        BigDecimal withdrawAmount = BigDecimal.TEN;
        Long clientId = 999L;

        when(clientService.getClientById(clientId)).thenThrow(ClientNotFoundException.class);

        // when & then
        assertThrows(ClientNotFoundException.class,
                () -> withdrawCommand.doWithdraw(clientId,withdrawAmount));

        verify(transactionCommand,
                never()).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                never()).saveAccount(any(Account.class));
    }

    @Test
    void doWithdraw_should_throw_insufficient_funds_exception_if_account_balance_is_lower_than_withdraw()
            throws ClientNotFoundException
    {
        // given
        BigDecimal accountBalance = BigDecimal.ONE;
        BigDecimal withdrawAmount = BigDecimal.TEN;
        Client client = prepareClient(accountBalance);

        when(clientService.getClientById(client.getClientId())).thenReturn(client);

        // when & then
        assertThrows(InsufficientFundsException.class,
                () -> withdrawCommand.doWithdraw(client.getClientId(),withdrawAmount));

        verify(transactionCommand,
                never()).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                never()).saveAccount(any(Account.class));
    }

    @Test
    void doWithdraw_should_return_account_balance_if_account_balance_is_higher_than_withdraw()
            throws ClientNotFoundException,
                   InsufficientFundsException
    {
        // given
        BigDecimal accountBalance = BigDecimal.TEN;
        BigDecimal withdrawAmount = BigDecimal.ONE;
        Client client = prepareClient(accountBalance);

        when(clientService.getClientById(client.getClientId())).thenReturn(client);

        BigDecimal expectedResult = BigDecimal.valueOf(9);

        // when
        BigDecimal retrievedResult = withdrawCommand.doWithdraw(client.getClientId(),withdrawAmount);

        // then
        assertEquals(expectedResult,retrievedResult);

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
    }

    @Test
    void doWithdraw_should_return_account_balance_if_account_balance_is_equal_than_withdraw()
            throws ClientNotFoundException,
                   InsufficientFundsException
    {
        // given
        BigDecimal accountBalance = BigDecimal.ONE;
        BigDecimal withdrawAmount = BigDecimal.ONE;
        Client client = prepareClient(accountBalance);

        when(clientService.getClientById(client.getClientId())).thenReturn(client);

        BigDecimal expectedResult = BigDecimal.ZERO;

        // when
        BigDecimal retrievedResult = withdrawCommand.doWithdraw(client.getClientId(),withdrawAmount);

        // then
        assertEquals(expectedResult,retrievedResult);

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
    }
}