package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class AccountDto {
    private Long accountId;
    private BigDecimal balance;
    private List<TransactionDto> transactionList;
}
