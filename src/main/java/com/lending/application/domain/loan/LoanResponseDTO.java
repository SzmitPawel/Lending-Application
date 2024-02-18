package com.lending.application.domain.loan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class LoanResponseDTO {
    @Schema(description = "Loan id.", example = "1")
    private Long loanId;
    @Schema(description = "Loan amount.", example = "1500.00")
    private BigDecimal loanAmount;
    @Schema(description = "Loan monthly payment.", example = "150.00")
    private BigDecimal monthlyPayment;
    @Schema(description = "Loan interest.", example = "5.75")
    private Float interest;
    @Schema(description = "When loan started.", example = "2024-01-30")
    private LocalDate loanStartDate;
    @Schema(description = "Loan period.", example = "120")
    private Integer repaymentPeriod;
}