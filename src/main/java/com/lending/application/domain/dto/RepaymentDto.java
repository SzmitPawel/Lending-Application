package com.lending.application.domain.dto;

import com.lending.application.domain.Loan;
import com.lending.application.domain.Penalty;
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
    private LoanDto loanDto;
    private PenaltyDto penaltyDto;
}
