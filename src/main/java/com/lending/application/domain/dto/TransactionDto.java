package com.lending.application.domain.dto;

import com.lending.application.domain.Account;
import com.lending.application.domain.TransactionMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TransactionDto {
    private Long paymentID;
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private TransactionMethodEnum transactionMethodEnum;
    private AccountDto account;
}
