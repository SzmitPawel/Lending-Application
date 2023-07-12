package com.lending.application.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    private Long transactionID;
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private TransactionMethodEnum transactionMethodEnum;
    private Account account;

    public Transaction() {
    }

    public Transaction(
            final BigDecimal transactionAmount,
            final LocalDate transactionDate,
            final TransactionMethodEnum transactionMethodEnum
    ) {
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
        this.transactionMethodEnum = transactionMethodEnum;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public Long getTransactionID() {
        return transactionID;
    }

    @Column(name = "TRANSACTION_AMOUNT", nullable = false)
    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    @Column(name = "TRANSACTION_DATE")
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    @Column(name = "TRANSACTION_METHOD")
    public TransactionMethodEnum getTransactionMethodEnum() {
        return transactionMethodEnum;
    }

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    public Account getAccount() {
        return account;
    }

    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionMethodEnum(TransactionMethodEnum transactionMethodEnum) {
        this.transactionMethodEnum = transactionMethodEnum;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}