package com.lending.application.service.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanCalculationDto {
    private BigDecimal amountOfCredit;
    private BigDecimal interestRate;
    private int numberOfMonths;
    private BigDecimal monthlyPayment;
}
