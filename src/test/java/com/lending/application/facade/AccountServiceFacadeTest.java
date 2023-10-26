package com.lending.application.facade;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceFacadeTest {
    @InjectMocks
    private AccountServiceFacade accountServiceFacade;
    @Mock
    private AccountService accountService;
    @Mock
    private ClientService clientService;
    @Mock
    private TransactionServiceFacade transactionServiceFacade;

    private Client prepareClient() {
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(BigDecimal.valueOf(1000.00));

        client.setAccount(account);

        return client;
    }

    @Test
    void testDeposit_shouldReturnAccountBalance() throws ClientNotFoundException, AccountNotFoundException {
        // given
        Client client = prepareClient();

        BigDecimal deposit = BigDecimal.valueOf(100.00);
        BigDecimal expectedAccountBalance = BigDecimal.valueOf(1100.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when
        BigDecimal retrievedAccountBalance = accountServiceFacade.deposit(client.getClientId(),deposit);

        // then
        verify(transactionServiceFacade,times(1))
                .createTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,times(1)).saveAccount(any(Account.class));

        assertEquals(expectedAccountBalance,retrievedAccountBalance);
    }

    @Test
    void testWithdraw_shouldThrowInsufficientFundsException() throws ClientNotFoundException {
        Client client = prepareClient();
        client.getAccount().setBalance(BigDecimal.ZERO);

        BigDecimal withdraw = BigDecimal.valueOf(100.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when & then
        assertThrows(InsufficientFundsException.class, () -> accountServiceFacade.withdraw(client.getClientId(),withdraw));
    }

    @Test
    void testWithdraw_shouldReturnAccountBalance() throws ClientNotFoundException, InsufficientFundsException {
        // given
        Client client = prepareClient();

        BigDecimal withdraw = BigDecimal.valueOf(100.00);
        BigDecimal expectedAccountBalance = BigDecimal.valueOf(900.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when
        BigDecimal retrievedAccountBalance = accountServiceFacade.withdraw(client.getClientId(),withdraw);

        // then
        verify(transactionServiceFacade,times(1))
                .createTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,times(1)).saveAccount(any(Account.class));

        assertEquals(expectedAccountBalance,retrievedAccountBalance);
    }

    @Test
    void testGetBalance_shouldReturnAccountBalance() throws ClientNotFoundException, AccountNotFoundException {
        // given
        Client client = prepareClient();

        BigDecimal expectedAccountBalance = BigDecimal.valueOf(1000.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);
        when(accountService.getAccountById(anyLong())).thenReturn(client.getAccount());

        // when
        BigDecimal retrievedBalance = accountServiceFacade.getBalance(client.getClientId());

        // then
        assertEquals(expectedAccountBalance,retrievedBalance);
    }
}