package com.lending.application.mapper;

import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanMapper {
    public LoanDto mapToLoanDto(final Loan loan) {
        return new LoanDto(
                loan.getLoanId(),
                loan.getLoanAmount(),
                loan.getInterest(),
                loan.getLoanStartDate(),
                loan.getRepaymentPeriod()
        );
    }

    public Loan mapToLona(final LoanDto loanDto) {
        Loan loan = new Loan();
        loan.setLoanId(loanDto.getLoanId());
        loan.setLoanAmount(loanDto.getLoanAmount());
        loan.setInterest(loanDto.getInterest());
        loan.setLoanStartDate(loanDto.getLoanStartDate());
        loan.setRepaymentPeriod(loan.getRepaymentPeriod());

        return loan;
    }

    public List<LoanDto> mapToLoanDtoList(final List<Loan> loanList) {
        return loanList.stream()
                .map(this::mapToLoanDto)
                .collect(Collectors.toList());
    }

    public List<Loan> mapToLoanList(final List<LoanDto> loanDtoList) {
        return loanDtoList.stream()
                .map(this::mapToLona)
                .collect(Collectors.toList());
    }
}