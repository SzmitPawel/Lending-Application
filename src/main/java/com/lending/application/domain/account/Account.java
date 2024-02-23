package com.lending.application.domain.account;

import com.lending.application.domain.transaction.Transaction;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ACCOUNT")
public class Account {
    private Long accountId;
    private BigDecimal balance;
    private List<Transaction> transactionList = new ArrayList<>();

    public Account() {
    }

    public Account(final BigDecimal balance) {
        this.balance = balance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public Long getAccountId() {
        return accountId;
    }

    @Column(name = "BALANCE")
    public BigDecimal getBalance() {
        return balance;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account", targetEntity = Transaction.class)
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}