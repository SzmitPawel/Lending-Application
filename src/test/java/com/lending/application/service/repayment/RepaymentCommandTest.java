package com.lending.application.service.repayment;

import com.lending.application.domain.*;
import com.lending.application.domain.account.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.loan.LoanService;
import com.lending.application.service.transaction.TransactionCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentCommandTest {
    @InjectMocks
    RepaymentCommand repaymentCommand;
    @Mock
    private LoanService loanService;
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

    private Loan prepareLoan() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanAmount(BigDecimal.valueOf(120.00));
        loan.setLoanStartDate(LocalDate.now());
        loan.setMonthlyPayment(BigDecimal.valueOf(10.00));
        loan.setInterest(5.75F);
        loan.setRepaymentPeriod(12);

        return loan;
    }

    @Test
    void doRepayment_should_throw_insufficient_funds_exception_if_account_balance_is_low()
            throws LoanNotFoundException
    {
        // given
        BigDecimal accountBalance = BigDecimal.ONE;

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when & then
        assertThrows(InsufficientFundsException.class,
                () -> repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount));

        verify(transactionCommand,
                never()).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                never()).saveAccount(any(Account.class));
        verify(loanService,
                never()).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_throw_loan_not_found_exception_if_loan_not_exist()
            throws LoanNotFoundException
    {
        // given
        Long loanId = 999L;
        BigDecimal repaymentAmount = BigDecimal.ZERO;

        when(loanService.getLoanById(loanId)).thenThrow(LoanNotFoundException.class);

        // when & then
        assertThrows(LoanNotFoundException.class,
                () -> repaymentCommand.doRepayment(loanId,repaymentAmount));

        verify(transactionCommand,
                never()).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                never()).saveAccount(any(Account.class));
        verify(loanService,
                never()).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_return_correct_account_balance()
            throws LoanNotFoundException,
                   InsufficientFundsException
    {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;
        BigDecimal expectedResult = BigDecimal.valueOf(90.00);

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        BigDecimal retrievedAccountBalance = repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount);

        // then
        assertNotNull(retrievedAccountBalance);
        assertEquals(expectedResult,retrievedAccountBalance);

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
        verify(loanService,
                times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_create_repayment()
            throws LoanNotFoundException,
                   InsufficientFundsException
    {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        Repayment expectedRepayment = new Repayment();
        expectedRepayment.setRepaymentAmount(repaymentAmount);
        expectedRepayment.setRepaymentDate(LocalDate.now());

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount);

        // then
        assertEquals(1,loan.getRepaymentList().size());
        assertEquals(expectedRepayment.getRepaymentAmount(),loan.getRepaymentList().get(0).getRepaymentAmount());
        assertEquals(expectedRepayment.getRepaymentDate(),loan.getRepaymentList().get(0).getRepaymentDate());

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
        verify(loanService,
                times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_created_repayment_and_be_correctly_associated_with_loan()
            throws LoanNotFoundException,
                   InsufficientFundsException
    {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount);

        // then
        assertEquals(1,loan.getRepaymentList().size());
        assertEquals(loan,loan.getRepaymentList().get(0).getLoan());

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class),any(BigDecimal.class),any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
        verify(loanService,
                times(1)).saveLoan(any(Loan.class));
    }
}