package com.lending.application.domain.dto;

import com.lending.application.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ClientDto {
    private Long clientId;
    private String name;
    private String lastName;
    private String address;
    private String emailAddress;
    private String phoneNumber;
}
