package com.lending.application.domain.dto;

import com.lending.application.domain.TransactionMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TransactionDto {
    private Long transactionID;
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private TransactionMethodEnum transactionMethodEnum;
}
