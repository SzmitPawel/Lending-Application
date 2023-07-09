package com.lending.application.mapper;

import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class LoanMapper {
    ClientMapper clientMapper;
    PenaltyMapper penaltyMapper;
    RepaymentMapper repaymentMapper;

    public LoanDto mapToLoanDto(final Loan loan) {
        return new LoanDto(
                loan.getLoanId(),
                loan.getLoanAmount(),
                loan.getInterest(),
                loan.getLoanStartDate(),
                loan.getRepaymentPeriod(),
                clientMapper.mapToClientDto(loan.getClient()),
                repaymentMapper.mapToListRepaymentDto(loan.getRepaymentList())
        );
    }

    public Loan mapToLoan(final LoanDto loanDto) {
        return new Loan(
                loanDto.getLoanId(),
                loanDto.getLoanAmount(),
                loanDto.getInterest(),
                loanDto.getLoanStartDate(),
                loanDto.getRepaymentPeriod(),
                clientMapper.mapToClient(loanDto.getClientDto()),
                repaymentMapper.mapToListRepayments(loanDto.getRepaymentList())
        );
    }

    public List<LoanDto> mapToListLoanDto(final List<Loan> loanList) {
        return loanList.stream()
                .map(this::mapToLoanDto)
                .collect(Collectors.toList());
    }

    public List<Loan> mapToLoanList(final List<LoanDto> loanDtoList) {
        return loanDtoList.stream()
                .map(this::mapToLoan)
                .collect(Collectors.toList());
    }
}