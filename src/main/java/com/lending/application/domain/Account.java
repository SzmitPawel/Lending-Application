package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "ACCOUNT")
public class Account {
    private Long accountId;
    private BigDecimal balance;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Long getAccountId() {
        return accountId;
    }

    @Column(name = "BALANCE")
    public BigDecimal getBalance() {
        return balance;
    }
}
