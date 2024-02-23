package com.lending.application.service.account;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DepositCommand {
    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionCommand transactionCommand;

    public BigDecimal doDeposit(
            final Long clientId,
            final BigDecimal depositAmount
    )
            throws ClientNotFoundException {
        Account account = clientService.getClientById(clientId).getAccount();

        addDepositToAccount(account, depositAmount);
        transactionCommand.doTransaction(account, depositAmount, TransactionMethodEnum.DEPOSIT);
        accountService.saveAccount(account);

        return account.getBalance();
    }

    private void addDepositToAccount(Account account, final BigDecimal depositAmount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(depositAmount);
        account.setBalance(newBalance);
    }
}