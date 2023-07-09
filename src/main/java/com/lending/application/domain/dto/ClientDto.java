package com.lending.application.domain.dto;

import com.lending.application.domain.Account;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.Loan;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ClientDto {
    private Long clientID;
    private String name;
    private String lastName;
    private String address;
    private String emailAddress;
    private String phoneNumber;
    private Account account;
    private CreditRating creditRating;
    private List<Loan> loanList;
}
