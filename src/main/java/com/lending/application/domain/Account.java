package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    public Account() {
    }

    public Account(final BigDecimal balance) {
        this.balance = balance;
    }
}
