package com.lending.application.domain.dto;

import com.lending.application.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class AccountDto {
    private Long accountId;
    private BigDecimal balance;
    private List<Transaction> transactionList;
}
