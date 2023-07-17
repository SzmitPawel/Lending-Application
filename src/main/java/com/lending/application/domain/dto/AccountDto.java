package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AccountDto {
    private Long accountId;
    private BigDecimal balance;
}
