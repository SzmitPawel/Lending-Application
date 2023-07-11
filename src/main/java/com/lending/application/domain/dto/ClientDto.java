package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientDto {
    private Long clientID;
    private String name;
    private String lastName;
    private String address;
    private String emailAddress;
    private String phoneNumber;
}
