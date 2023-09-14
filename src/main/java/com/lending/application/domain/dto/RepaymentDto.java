package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepaymentDto {
    private Long repaymentId;
    private BigDecimal repaymentAmount;
    private LocalDate repaymentDate;
}
