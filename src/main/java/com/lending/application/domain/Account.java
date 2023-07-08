package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long accountId;
    @Column(name = "BALANCE")
    private BigDecimal balance;
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "account", targetEntity = Transaction.class)
    private List<Transaction> transactionList = new ArrayList<>();

    public Account() {
    }

    public Account(final BigDecimal balance) {
        this.balance = balance;
    }
}
