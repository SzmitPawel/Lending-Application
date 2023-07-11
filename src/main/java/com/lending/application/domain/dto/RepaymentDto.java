package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RepaymentDto {
    private Long repaymentId;
    private BigDecimal repaymentAmount;
    private LocalDate repaymentDate;
}
