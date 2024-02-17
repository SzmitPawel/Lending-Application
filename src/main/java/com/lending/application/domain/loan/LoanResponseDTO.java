package com.lending.application.domain.loan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class LoanResponseDTO {
    private Long loanId;
    private BigDecimal loanAmount;
    private BigDecimal monthlyPayment;
    private Float interest;
    private LocalDate loanStartDate;
    private Integer repaymentPeriod;
}
