package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositCommandTest {
    @InjectMocks
    private DepositCommand depositCommand;
    @Mock
    private ClientService clientService;

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
        assertEquals(expectedResult,retrievedAccountBalance);
    }

    @Test
    public void deposit_should_create_transaction_if_do_deposit_succeed()
            throws ClientNotFoundException {

        // given
        BigDecimal accountBalance = BigDecimal.TEN;
        Client client = prepareClient(accountBalance);
        BigDecimal depositAmount = BigDecimal.TEN;
        Long clientId = client.getClientId();

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setTransactionAmount(BigDecimal.TEN);
        expectedTransaction.setTransactionDate(LocalDate.now());
        expectedTransaction.setTransactionMethodEnum(TransactionMethodEnum.DEPOSIT);

        when(clientService.getClientById(clientId)).thenReturn(client);

        // when
        depositCommand.doDeposit(clientId,depositAmount);
        Transaction retrievedTransaction = client.getAccount().getTransactionList().get(0);

        // then
        assertEquals(expectedTransaction.getTransactionDate(),retrievedTransaction.getTransactionDate());
        assertEquals(expectedTransaction.getTransactionAmount(),retrievedTransaction.getTransactionAmount());
        assertEquals(expectedTransaction.getTransactionMethodEnum(),retrievedTransaction.getTransactionMethodEnum());
    }
}