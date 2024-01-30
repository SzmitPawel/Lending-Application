package com.lending.application.domain.client;

import com.lending.application.domain.account.Account;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.Loan;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CLIENT")
public class Client {
    private Long clientId;
    private String name;
    private String lastName;
    private String address;
    private String emailAddress;
    private String phoneNumber;
    private Account account;
    private CreditRating creditRating;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public Long getClientId() {
        return clientId;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    @Column(name = "LAST_NAME", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }

    @Column(name = "EMAIL_ADDRESS")
    public String getEmailAddress() {
        return emailAddress;
    }

    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    public Account getAccount() {
        return account;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "RATING_ID")
    public CreditRating getCreditRating() {
        return creditRating;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client", targetEntity = Loan.class)
    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setClientId(Long clientID) {
        this.clientId = clientID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCreditRating(CreditRating creditRating) {
        this.creditRating = creditRating;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }
}