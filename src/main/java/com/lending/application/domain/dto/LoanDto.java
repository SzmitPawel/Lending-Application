package com.lending.application.domain.dto;

import com.lending.application.domain.Client;
import com.lending.application.domain.Repayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class LoanDto {
    private Long loanId;
    private BigDecimal loanAmount;
    private Float interest;
    private LocalDate loanStartDate;
    private Integer repaymentPeriod;
    private Client client;
    List<Repayment> repaymentList;
}
