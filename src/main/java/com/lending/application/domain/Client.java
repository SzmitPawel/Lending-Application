package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Table(name = "CLIENT")
public class Client {
    private Long clientID;
    private String name;
    private String lastName;
    private String address;
    private String emailAddress;
    private String phoneNumber;

    private Account account;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getClientID() {
        return clientID;
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
}