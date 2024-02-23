package com.lending.application.service.account;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WithdrawCommand {
    private final ClientService clientService;
    private final TransactionCommand transactionCommand;
    private final AccountService accountService;

    public BigDecimal doWithdraw(final Long clientId, final BigDecimal withdrawAmount)
            throws ClientNotFoundException,
            InsufficientFundsException {
        Client client = clientService.getClientById(clientId);
        Account account = client.getAccount();

        if (checkAccountBalance(account, withdrawAmount)) {
            transactionCommand.doTransaction(account, withdrawAmount, TransactionMethodEnum.WITHDRAWAL);
            updateAccountBalance(account, withdrawAmount);
            accountService.saveAccount(account);

            return account.getBalance();
        } else {
            throw new InsufficientFundsException();
        }
    }

    private boolean checkAccountBalance(final Account account, final BigDecimal withdrawAmount) {
        return account.getBalance().compareTo(withdrawAmount) >= 0 ? true : false;
    }

    private void updateAccountBalance(Account account, final BigDecimal withdrawAmount) {
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance = balance.subtract(withdrawAmount);
        account.setBalance(newBalance);
    }
}