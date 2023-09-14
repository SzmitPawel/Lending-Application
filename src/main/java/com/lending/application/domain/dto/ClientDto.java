package com.lending.application.domain.dto;

import com.lending.application.domain.Account;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long clientId;
    private String name;
    private String lastName;
    private String address;
    private String emailAddress;
    private String phoneNumber;
}
