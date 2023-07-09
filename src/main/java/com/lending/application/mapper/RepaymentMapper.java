package com.lending.application.mapper;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.dto.RepaymentDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RepaymentMapper {
    LoanMapper loanMapper;
    PenaltyMapper penaltyMapper;

    public RepaymentDto mapToRepaymentDto(final Repayment repayment) {
        return new RepaymentDto(
                repayment.getRepaymentId(),
                repayment.getRepaymentAmount(),
                repayment.getRepaymentDate(),
                loanMapper.mapToLoanDto(repayment.getLoan()),
                penaltyMapper.mapToPenaltyDto(repayment.getPenalty())
        );
    }

    public Repayment mapToRepayment(final RepaymentDto repaymentDto) {
        return new Repayment(
                repaymentDto.getRepaymentId(),
                repaymentDto.getRepaymentAmount(),
                repaymentDto.getRepaymentDate(),
                loanMapper.mapToLoan(repaymentDto.getLoanDto()),
                penaltyMapper.mapToPenalty(repaymentDto.getPenaltyDto())
        );
    }

    public List<RepaymentDto> mapToListRepaymentDto(final List<Repayment> repaymentList) {
        return repaymentList.stream()
                .map(this::mapToRepaymentDto)
                .collect(Collectors.toList());
    }

    public List<Repayment> mapToListRepayments(final List<RepaymentDto> repaymentDtoList) {
        return repaymentDtoList.stream()
                .map(this::mapToRepayment)
                .collect(Collectors.toList());
    }
}