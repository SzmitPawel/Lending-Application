package com.lending.application.service.repayment;

import com.lending.application.domain.*;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.loan.LoanService;
import com.lending.application.service.repayment.RepaymentCommand;
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
class RepaymentCommandTest {
    @InjectMocks
    RepaymentCommand repaymentCommand;
    @Mock
    private LoanService loanService;
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
    void repayment_should_throw_insufficient_funds_exception_if_account_balance_is_low()
            throws LoanNotFoundException {

        // given
        BigDecimal accountBalance = BigDecimal.ONE;

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when & then
        assertThrows(InsufficientFundsException.class, () -> repaymentCommand
                .doRepayment(loan.getLoanId(),repaymentAmount));
    }

    @Test
    void repayment_should_throw_loan_not_found_exception_if_loan_not_exist()
            throws LoanNotFoundException {

        // given
        Long loanId = 999L;
        BigDecimal repaymentAmount = BigDecimal.ZERO;

        when(loanService.getLoanById(loanId)).thenThrow(LoanNotFoundException.class);

        // when & then
        assertThrows(LoanNotFoundException.class, () -> repaymentCommand.doRepayment(loanId,repaymentAmount));
    }

    @Test
    void repayment_should_return_repayment_amount_if_do_repayment_succeed()
            throws LoanNotFoundException, InsufficientFundsException {

        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;
        BigDecimal expectedResult = BigDecimal.TEN;

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        BigDecimal retrievedRepaymentAmount = repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount);

        // then
        assertEquals(expectedResult,retrievedRepaymentAmount);
    }

    @Test
    void repayment_should_create_repayment_if_do_repayment_succeed()
            throws LoanNotFoundException, InsufficientFundsException {

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
        Repayment retrievedRepayment = loan.getRepaymentList().get(0);

        // when
        assertEquals(expectedRepayment.getRepaymentAmount(),retrievedRepayment.getRepaymentAmount());
        assertEquals(expectedRepayment.getRepaymentDate(),retrievedRepayment.getRepaymentDate());
    }

    @Test
    void repayment_should_create_transaction_if_do_repayment_succeed()
            throws LoanNotFoundException, InsufficientFundsException {

        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setTransactionAmount(BigDecimal.TEN);
        expectedTransaction.setTransactionDate(LocalDate.now());
        expectedTransaction.setTransactionMethodEnum(TransactionMethodEnum.REPAYMENT);

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount);
        Transaction retrievedTransaction = client.getAccount().getTransactionList().get(0);

        // then
        assertEquals(expectedTransaction.getTransactionDate(),retrievedTransaction.getTransactionDate());
        assertEquals(expectedTransaction.getTransactionAmount(),retrievedTransaction.getTransactionAmount());
        assertEquals(expectedTransaction.getTransactionMethodEnum(),retrievedTransaction.getTransactionMethodEnum());
    }

    @Test
    void repayment_should_update_account_balance_if_do_repayment_succeed()
            throws LoanNotFoundException, InsufficientFundsException {

        // given
        BigDecimal accountBalance = BigDecimal.valueOf(100.00);

        Client client = prepareClient(accountBalance);
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        loan.setClient(client);

        BigDecimal repaymentAmount = BigDecimal.TEN;
        BigDecimal expectedAccountBalance = BigDecimal.valueOf(90.00);

        when(loanService.getLoanById(loan.getLoanId())).thenReturn(loan);

        // when
        repaymentCommand.doRepayment(loan.getLoanId(),repaymentAmount);
        BigDecimal retrievedAccountBalance = client.getAccount().getBalance();

        // then
        assertEquals(expectedAccountBalance,retrievedAccountBalance);
    }
}