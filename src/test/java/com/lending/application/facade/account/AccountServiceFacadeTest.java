package com.lending.application.facade.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.facade.TransactionServiceFacade;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.loan.LoanService;
import org.junit.jupiter.api.Nested;
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
    @Mock
    private RepaymentCommand repaymentCommand;
    @Mock
    private LoanService loanService;

    private Client prepareClient(final BigDecimal accountBalance) {
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(accountBalance);

        client.setAccount(account);

        return client;
    }

    @Test
    void testDeposit_shouldReturnAccountBalance() throws ClientNotFoundException, AccountNotFoundException {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(1000.00);
        Client client = prepareClient(accountBalance);

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
        BigDecimal accountBalance = BigDecimal.ZERO;
        Client client = prepareClient(accountBalance);;

        BigDecimal withdraw = BigDecimal.valueOf(100.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when & then
        assertThrows(InsufficientFundsException.class, () -> accountServiceFacade.withdraw(client.getClientId(),withdraw));
    }

    @Test
    void testWithdraw_shouldReturnAccountBalance() throws ClientNotFoundException, InsufficientFundsException {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(1000.00);
        Client client = prepareClient(accountBalance);

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
        BigDecimal accountBalance = BigDecimal.valueOf(1000.00);
        Client client = prepareClient(accountBalance);

        BigDecimal expectedAccountBalance = BigDecimal.valueOf(1000.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);
        when(accountService.getAccountById(anyLong())).thenReturn(client.getAccount());

        // when
        BigDecimal retrievedBalance = accountServiceFacade.getBalance(client.getClientId());

        // then
        assertEquals(expectedAccountBalance,retrievedBalance);
    }

    @Nested
    class Repayment {
        @Test
        void repayment_should_throw_loan_not_found_exception_if_loan_not_exist()
                throws LoanNotFoundException, InsufficientFundsException {

            // given
            Long loanId = 999L;
            BigDecimal repaymentAmount = BigDecimal.ONE;

            when(repaymentCommand.doRepayment(loanId,repaymentAmount)).thenThrow(LoanNotFoundException.class);

            // When & then
            assertThrows(LoanNotFoundException.class, () -> accountServiceFacade.repayment(loanId,repaymentAmount));
        }

        @Test
        void repayment_should_throw_insufficient_funds_exception_if_account_balance_is_too_low()
                throws InsufficientFundsException, LoanNotFoundException {

            // given
            Long loanId = 1L;
            BigDecimal repaymentAmount = BigDecimal.TEN;

            when(repaymentCommand.doRepayment(loanId,repaymentAmount)).thenThrow(InsufficientFundsException.class);

            // when & then
            assertThrows(InsufficientFundsException.class, () -> repaymentCommand.doRepayment(loanId,repaymentAmount));
        }

        @Test
        void repayment_should_return_repayment_amount_if_repayment_succeed()
                throws InsufficientFundsException, LoanNotFoundException {

            // given
            Long loanId = 1L;
            BigDecimal repaymentAmount = BigDecimal.TEN;

            when(repaymentCommand.doRepayment(loanId,repaymentAmount)).thenReturn(repaymentAmount);

            // when
            BigDecimal retrievedRepaymentAmount = accountServiceFacade.repayment(loanId,repaymentAmount);
            BigDecimal expectedResult = BigDecimal.TEN;

            // then
            assertEquals(expectedResult,retrievedRepaymentAmount);
        }
    }
}