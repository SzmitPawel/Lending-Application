package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class LoanDto {
    private Long loanId;
    private BigDecimal loanAmount;
    private Float interest;
    private LocalDate loanStartDate;
    private Integer repaymentPeriod;
}
