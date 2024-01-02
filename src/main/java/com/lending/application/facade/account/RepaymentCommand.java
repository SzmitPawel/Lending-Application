package com.lending.application.facade.account;

import com.lending.application.domain.*;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.loan.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RepaymentCommand {
    private final LoanService loanService;
    private final AccountService accountService;

    public BigDecimal doRepayment(final Long loanId, final BigDecimal repaymentAmount)
            throws LoanNotFoundException,
                   InsufficientFundsException {

        Loan retrievedLoan = loanService.getLoanById(loanId);
        Client retrievedClient = retrievedLoan.getClient();

        boolean checkAccountBalance = checkAccountBalance(retrievedClient,repaymentAmount);

        if (checkAccountBalance) {
            Repayment repayment = createRepayment(repaymentAmount);
            Transaction transaction = createTransaction(repaymentAmount);

            makeRepayment(retrievedLoan,repayment);
            makeTransaction(retrievedClient.getAccount(),transaction);
            updateAccountBalance(retrievedClient.getAccount(),repaymentAmount);
            saveToDataBase(retrievedClient.getAccount(),retrievedLoan);

            return repaymentAmount;
        } else {
            throw new InsufficientFundsException();
        }
    }

    private boolean checkAccountBalance(final Client client, final BigDecimal repaymentAmount) {
        Account account = client.getAccount();
        BigDecimal accountBalance = account.getBalance();

        return accountBalance.compareTo(repaymentAmount) >= 0 ? true : false;
    }

    private Repayment createRepayment(final BigDecimal repaymentAmount) {
        Repayment repayment = new Repayment();
        repayment.setRepaymentDate(LocalDate.now());
        repayment.setRepaymentAmount(repaymentAmount);

        return repayment;
    }

    private Transaction createTransaction(final BigDecimal repaymentAmount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionAmount(repaymentAmount);
        transaction.setTransactionMethodEnum(TransactionMethodEnum.REPAYMENT);

        return transaction;
    }

    private void makeRepayment(Loan retrievedLoan, Repayment repayment) {
        retrievedLoan.getRepaymentList().add(repayment);
        repayment.setLoan(retrievedLoan);
    }

    private void makeTransaction(Account account, Transaction transaction) {
        account.getTransactionList().add(transaction);
        transaction.setAccount(account);
    }

    private void updateAccountBalance(Account account, BigDecimal repaymentAmount) {
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance = balance.subtract(repaymentAmount);
        account.setBalance(newBalance);
    }

    private void saveToDataBase(final Account account, final Loan loan) {
        loanService.saveLoan(loan);
        accountService.saveAccount(account);
    }
}