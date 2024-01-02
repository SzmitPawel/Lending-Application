package com.lending.application.facade.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.service.account.AccountService;
import com.lending.application.service.client.ClientService;
import com.lending.application.facade.TransactionServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceFacade {
    private final AccountService accountService;
    private final ClientService clientService;
    private final TransactionServiceFacade transactionServiceFacade;
    private final RepaymentCommand repaymentCommand;

    public BigDecimal deposit(final Long clientId, final BigDecimal deposit)
            throws ClientNotFoundException, AccountNotFoundException {

        Client client = clientService.getClientById(clientId);

        Account account = client.getAccount();
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(deposit);
        account.setBalance(newBalance);

        transactionServiceFacade.createTransaction(account,deposit, TransactionMethodEnum.DEPOSIT);

        accountService.saveAccount(account);

        return newBalance;
    }

    public BigDecimal withdraw(final Long clientId, final BigDecimal withdraw)
            throws ClientNotFoundException, InsufficientFundsException {

        Client client = clientService.getClientById(clientId);

        Account account = client.getAccount();
        BigDecimal currentBalance = account.getBalance();

        if (withdraw.compareTo(currentBalance) == -1) {
            BigDecimal newBalance = currentBalance.subtract(withdraw);
            account.setBalance(newBalance);
            transactionServiceFacade.createTransaction(account,withdraw,TransactionMethodEnum.WITHDRAWAL);
            accountService.saveAccount(account);
            return newBalance;
        } else {
            throw new InsufficientFundsException();
        }
    }

    public BigDecimal repayment(final Long loanId, final BigDecimal repayment)
            throws InsufficientFundsException, LoanNotFoundException {

        return repaymentCommand.doRepayment(loanId,repayment);
    }

    public BigDecimal getBalance(final Long clientId) throws ClientNotFoundException, AccountNotFoundException {
        Client client = clientService.getClientById(clientId);

        Account account = accountService.getAccountById(client.getAccount().getAccountId());
        BigDecimal currentBalance = account.getBalance();

        return currentBalance;
    }
}
