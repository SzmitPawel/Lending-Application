package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.account.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.loan.Loan;
import com.lending.application.domain.transaction.TransactionMethodEnum;
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
import java.math.RoundingMode;
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

    private Loan prepareLoan(final LocalDate loanStartDate) {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanAmount(BigDecimal.valueOf(120.00));
        loan.setLoanStartDate(loanStartDate);
        loan.setMonthlyPayment(BigDecimal.valueOf(10.00));
        loan.setInterest(5.75F);
        loan.setRepaymentPeriod(12);

        return loan;
    }

    @Test
    void doRepayment_should_throw_insufficient_funds_exception_if_account_balance_is_low()
            throws LoanNotFoundException {
        // given
        BigDecimal accountBalance = BigDecimal.ONE;

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan(LocalDate.now());

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when & then
        assertThrows(InsufficientFundsException.class,
                () -> repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount));

        verify(transactionCommand,
                never()).doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService,
                never()).saveAccount(any(Account.class));
        verify(loanService,
                never()).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_throw_loan_not_found_exception_if_loan_not_exist()
            throws LoanNotFoundException {
        // given
        Long loanId = 999L;
        BigDecimal repaymentAmount = BigDecimal.ZERO;

        when(loanService.getLoanById(loanId)).thenThrow(LoanNotFoundException.class);

        // when & then
        assertThrows(LoanNotFoundException.class,
                () -> repaymentCommand.doRepayment(loanId, repaymentAmount));

        verify(transactionCommand,
                never()).doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService,
                never()).saveAccount(any(Account.class));
        verify(loanService,
                never()).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_return_correct_account_balance()
            throws LoanNotFoundException,
            InsufficientFundsException {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan(LocalDate.now());

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;
        BigDecimal expectedResult = BigDecimal.valueOf(90.00);

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        BigDecimal retrievedAccountBalance = repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount);

        // then
        assertNotNull(retrievedAccountBalance);
        assertEquals(expectedResult, retrievedAccountBalance);

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
        verify(loanService,
                times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_create_repayment()
            throws LoanNotFoundException,
            InsufficientFundsException {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan(LocalDate.now());

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        Repayment expectedRepayment = new Repayment();
        expectedRepayment.setRepaymentAmount(repaymentAmount);
        expectedRepayment.setRepaymentDate(LocalDate.now());

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount);

        // then
        assertEquals(1, loan.getRepaymentList().size());
        assertEquals(expectedRepayment.getRepaymentAmount(), loan.getRepaymentList().get(0).getRepaymentAmount());
        assertEquals(expectedRepayment.getRepaymentDate(), loan.getRepaymentList().get(0).getRepaymentDate());

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
        verify(loanService,
                times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_created_repayment_and_be_correctly_associated_with_loan()
            throws LoanNotFoundException,
            InsufficientFundsException {
        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan(LocalDate.now().minusDays(1));

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount);

        // then
        assertEquals(1, loan.getRepaymentList().size());
        assertEquals(loan, loan.getRepaymentList().get(0).getLoan());

        verify(transactionCommand,
                times(1)).doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService,
                times(1)).saveAccount(any(Account.class));
        verify(loanService,
                times(1)).saveLoan(any(Loan.class));
    }

    @Test
    void doRepayment_should_created_penalty_and_be_correctly_associated_with_repayment()
            throws LoanNotFoundException, InsufficientFundsException {
        // given
        BigDecimal repaymentAmount = BigDecimal.ONE;

        Client client = prepareClient(BigDecimal.TEN);
        Loan loan = prepareLoan(LocalDate.now().minusDays(2));

        client.getLoanList().add(loan);
        loan.setClient(client);

        when(loanService.getLoanById(anyLong())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount);

        // then
        verify(transactionCommand, times(1))
                .doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService, times(1)).saveAccount(any(Account.class));
        verify(loanService, times(1)).saveLoan(any(Loan.class));

        assertEquals(1, loan.getRepaymentList().size());
        assertNotNull(loan.getRepaymentList().get(0).getPenalty());
    }

    @Test
    void doRepayment_should_not_created_penalty_and_not_be_associated_with_repayment()
            throws LoanNotFoundException, InsufficientFundsException {
        // given
        BigDecimal repaymentAmount = BigDecimal.ONE;

        Client client = prepareClient(BigDecimal.TEN);
        Loan loan = prepareLoan(LocalDate.now());

        client.getLoanList().add(loan);
        loan.setClient(client);

        when(loanService.getLoanById(anyLong())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount);

        // then
        verify(transactionCommand, times(1))
                .doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService, times(1)).saveAccount(any(Account.class));
        verify(loanService, times(1)).saveLoan(any(Loan.class));

        assertEquals(1, loan.getRepaymentList().size());
        assertNull(loan.getRepaymentList().get(0).getPenalty());
    }

    @Test
    void doRepayment_should_add_penalty_to_repayment_amount()
            throws LoanNotFoundException, InsufficientFundsException {
        // given
        BigDecimal repaymentAmount = BigDecimal.valueOf(1000.00);

        Client client = prepareClient(BigDecimal.valueOf(1500.00));
        Loan loan = prepareLoan(LocalDate.now().minusDays(1));

        client.getLoanList().add(loan);
        loan.setClient(client);

        when(loanService.getLoanById(anyLong())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(), repaymentAmount);

        // then
        verify(transactionCommand, times(1))
                .doTransaction(any(Account.class), any(BigDecimal.class), any(TransactionMethodEnum.class));
        verify(accountService, times(1)).saveAccount(any(Account.class));
        verify(loanService, times(1)).saveLoan(any(Loan.class));

        assertEquals(BigDecimal.valueOf(1050.00).setScale(2, RoundingMode.HALF_UP),
                client.getLoanList().get(0).getRepaymentList().get(0).getRepaymentAmount());
    }
}