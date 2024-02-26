package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.account.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.loan.Loan;
import com.lending.application.domain.penalty.Penalty;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.loan.LoanService;
import com.lending.application.service.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RepaymentCommand {
    private final LoanService loanService;
    private final AccountService accountService;
    private final TransactionCommand transactionCommand;

    private final BigDecimal PENALTY = BigDecimal.valueOf(0.05);

    public BigDecimal doRepayment(final Long loanId, final BigDecimal repaymentAmount)
            throws LoanNotFoundException,
            InsufficientFundsException {
        Loan loan = loanService.getLoanById(loanId);
        Client client = loan.getClient();
        Account account = client.getAccount();
        BigDecimal loanRepaymentAmount = repaymentAmount;

        if (checkPenalty(loan)) {
            loanRepaymentAmount = loanRepaymentAmount
                    .multiply(PENALTY)
                    .add(repaymentAmount)
                    .setScale(2, RoundingMode.HALF_UP);

        }

        if (checkAccountBalance(account, loanRepaymentAmount)) {
            if (checkPenalty(loan)) {
                Repayment repayment = createRepayment(loanRepaymentAmount);
                Penalty penalty = createPenalty(PENALTY);
                makePenalty(repayment, penalty);
                makeRepayment(loan, repayment);
            } else {
                makeRepayment(loan, createRepayment(loanRepaymentAmount));
            }
            transactionCommand.doTransaction(account, loanRepaymentAmount, TransactionMethodEnum.REPAYMENT);
            updateAccountBalance(account, loanRepaymentAmount);
            accountService.saveAccount(account);
            loanService.saveLoan(loan);
        } else {
            throw new InsufficientFundsException();
        }

        return account.getBalance();
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

    private boolean checkPenalty(final Loan loan) {
        LocalDate repaymentLoanDay = loan.getLoanStartDate();
        LocalDate currentDay = LocalDate.now();

        return currentDay.getDayOfMonth() > repaymentLoanDay.getDayOfMonth() ? true : false;
    }

    private Penalty createPenalty(final BigDecimal penaltyPercentage) {
        Penalty penalty = new Penalty();
        penalty.setPenaltyDate(LocalDate.now());
        penalty.setPenaltyPercentage(penaltyPercentage.intValue());

        return penalty;
    }

    private void makePenalty(Repayment repayment, final Penalty penalty) {
        repayment.setPenalty(penalty);
    }
}