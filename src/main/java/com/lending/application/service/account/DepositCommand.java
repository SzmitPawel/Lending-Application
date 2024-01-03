package com.lending.application.service.account;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DepositCommand {
    private final ClientService clientService;

    public BigDecimal doDeposit(final Long clientId, final BigDecimal depositAmount)
            throws ClientNotFoundException {

        Client client = clientService.getClientById(clientId);
        Account account = client.getAccount();
        Transaction transaction = createTransaction(depositAmount);

        BigDecimal newBalance = addedToAccount(account,depositAmount);
        makeTransaction(account,transaction);
        saveToDataBase(client);

        return newBalance;
    }

    private BigDecimal addedToAccount(Account account, final BigDecimal depositAmount) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal newBalance = currentBalance.add(depositAmount);
        account.setBalance(newBalance);

        return newBalance;
    }

    private Transaction createTransaction(final BigDecimal depositAmount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionAmount(depositAmount);
        transaction.setTransactionMethodEnum(TransactionMethodEnum.DEPOSIT);

        return transaction;
    }

    private void makeTransaction(Account account, Transaction transaction) {
        account.getTransactionList().add(transaction);
        transaction.setAccount(account);
    }

    private void saveToDataBase(final Client client) {
        clientService.saveClient(client);
    }
}