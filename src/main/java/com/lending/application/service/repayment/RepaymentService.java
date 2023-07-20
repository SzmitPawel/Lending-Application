package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.dto.RepaymentDto;
import com.lending.application.exception.RepaymentNotFoundException;
import com.lending.application.mapper.RepaymentMapper;
import com.lending.application.repository.RepaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RepaymentService {
    RepaymentRepository repaymentRepository;
    RepaymentMapper repaymentMapper;

    public void createRepayment(final RepaymentDto repaymentDto) {
        repaymentRepository.saveAndFlush(repaymentMapper.mapToRepayment(repaymentDto));
    }

    public RepaymentDto getRepaymentById(final Long repaymentID) throws RepaymentNotFoundException {
        Repayment repayment = repaymentRepository
                .findById(repaymentID)
                .orElseThrow(RepaymentNotFoundException::new);

        return repaymentMapper.mapToRepaymentDto(repayment);
    }

    public List<RepaymentDto> getAllRepayments() {
        return repaymentMapper.mapToRepaymentDtoList(repaymentRepository.findAll());
    }

    public void updateRepayment(RepaymentDto repaymentDto) throws RepaymentNotFoundException {
        Repayment repayment = repaymentRepository
                .findById(repaymentDto.getRepaymentId())
                .orElseThrow(RepaymentNotFoundException::new);

        repayment.setRepaymentAmount(repaymentDto.getRepaymentAmount());
        repayment.setRepaymentDate(repaymentDto.getRepaymentDate());

        repaymentRepository.saveAndFlush(repayment);
    }

    public void deleteRepaymentById(final Long repaymentId) throws RepaymentNotFoundException {
        if (repaymentRepository.findById(repaymentId).isPresent()) {
            repaymentRepository.deleteById(repaymentId);
        } else {
            throw new RepaymentNotFoundException();
        }
    }
}