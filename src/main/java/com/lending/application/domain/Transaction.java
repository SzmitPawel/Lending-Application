package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "PAYMENT")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long paymentID;
    @Column(name = "PAYMENT_AMOUNT", nullable = false)
    private BigDecimal transactionAmount;
    @Column(name = "PAYMENT_DATE")
    private LocalDate transactionDate;
    @Column(name = "PAYMENT_METHOD")
    private TransactionMethodEnum transactionMethodEnum;
    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    public Transaction() {
    }

    public Transaction(
            final BigDecimal paymentAmount,
            final LocalDate paymentDate,
            final TransactionMethodEnum transactionMethodEnum
    ) {
        this.transactionAmount = paymentAmount;
        this.transactionDate = paymentDate;
        this.transactionMethodEnum = transactionMethodEnum;
    }

    public Transaction(
            final Long paymentID,
            final BigDecimal paymentAmount,
            final LocalDate paymentDate,
            final TransactionMethodEnum transactionMethodEnum,
            final Account account
    ) {
        this.paymentID = paymentID;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.transactionMethodEnum = transactionMethodEnum;
        this.account = account;
    }
}
