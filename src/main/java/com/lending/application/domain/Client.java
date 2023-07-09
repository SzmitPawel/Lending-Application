package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CLIENT")
public class Client {
    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long clientID;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "RATING_ID")
    private CreditRating creditRating;
    @Setter(AccessLevel.PRIVATE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "client",targetEntity = Loan.class)
    private List<Loan> loanList = new ArrayList<>();

    public Client() {
    }

    public Client(
            final String name,
            final String lastName,
            final String address,
            final String emailAddress,
            final String phoneNumber
    ) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public Client(
            final Long clientID,
            final String name,
            final String lastName,
            final String address,
            final String emailAddress,
            final String phoneNumber,
            final Account account,
            final CreditRating creditRating,
            final List<Loan> loanList
    ) {
        this.clientID = clientID;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.account = account;
        this.creditRating = creditRating;
        this.loanList = loanList;
    }
}