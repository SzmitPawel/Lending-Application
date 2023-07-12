package com.lending.application.mapper;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.dto.RepaymentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RepaymentMapper {
    public RepaymentDto mapToRepaymentDto(final Repayment repayment) {
        return new RepaymentDto(
                repayment.getRepaymentId(),
                repayment.getRepaymentAmount(),
                repayment.getRepaymentDate()
        );
    }

    public Repayment mapToRepayment(final RepaymentDto repaymentDto) {
        Repayment repayment = new Repayment();
        repayment.setRepaymentId(repaymentDto.getRepaymentId());
        repayment.setRepaymentAmount(repaymentDto.getRepaymentAmount());
        repayment.setRepaymentDate(repaymentDto.getRepaymentDate());

        return repayment;
    }

    public List<RepaymentDto> mapToRepaymentDtoList(final List<Repayment> repaymentList) {
        return repaymentList.stream()
                .map(this::mapToRepaymentDto)
                .collect(Collectors.toList());
    }

    public List<Repayment> mapToRepaymentList(final  List<RepaymentDto> repaymentDtoList) {
        return repaymentDtoList.stream()
                .map(this::mapToRepayment)
                .collect(Collectors.toList());
    }
}