package com.lending.application.service.repayment;

import com.lending.application.domain.*;
import com.lending.application.domain.account.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.loan.Loan;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.loan.LoanService;
import com.lending.application.service.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RepaymentCommand {
    private final LoanService loanService;
    private final AccountService accountService;
    private final TransactionCommand transactionCommand;

    public BigDecimal doRepayment(final Long loanId,final BigDecimal repaymentAmount)
            throws LoanNotFoundException,
                   InsufficientFundsException
    {
        Loan loan = loanService.getLoanById(loanId);
        Client client = loan.getClient();
        Account account = client.getAccount();

        if (checkAccountBalance(account,repaymentAmount)) {
            makeRepayment(loan,createRepayment(repaymentAmount));
            transactionCommand.doTransaction(account,repaymentAmount, TransactionMethodEnum.REPAYMENT);
            updateAccountBalance(account,repaymentAmount);
            accountService.saveAccount(account);
            loanService.saveLoan(loan);

            return account.getBalance();
        } else {
            throw new InsufficientFundsException();
        }
    }

    private boolean checkAccountBalance(final Account account, final BigDecimal repaymentAmount) {
        BigDecimal accountBalance = account.getBalance();

        return accountBalance.compareTo(repaymentAmount) >= 0 ? true : false;
    }

    private Repayment createRepayment(final BigDecimal repaymentAmount) {
        Repayment repayment = new Repayment();
        repayment.setRepaymentDate(LocalDate.now());
        repayment.setRepaymentAmount(repaymentAmount);

        return repayment;
    }

    private void makeRepayment(Loan retrievedLoan, Repayment repayment) {
        retrievedLoan.getRepaymentList().add(repayment);
        repayment.setLoan(retrievedLoan);
    }

    private void updateAccountBalance(Account account, BigDecimal repaymentAmount) {
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance = balance.subtract(repaymentAmount);
        account.setBalance(newBalance);
    }
}