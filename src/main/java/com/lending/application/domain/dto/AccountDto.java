package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDto {
    private Long accountId;
    private BigDecimal balance;
    private List<Long> transactionIds;
}
