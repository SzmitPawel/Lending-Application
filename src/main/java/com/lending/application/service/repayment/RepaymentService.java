package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.dto.RepaymentDto;
import com.lending.application.exception.RepaymentNotFoundException;
import com.lending.application.mapper.RepaymentMapper;
import com.lending.application.repository.RepaymentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepaymentService {
    private final RepaymentRepository repaymentRepository;
    private final RepaymentMapper repaymentMapper;

    public RepaymentDto createRepayment(final RepaymentDto repaymentDto) {
        Repayment repayment = repaymentMapper.mapToRepayment(repaymentDto);
        Repayment createdRepayment = repaymentRepository.saveAndFlush(repayment);

        return repaymentMapper.mapToRepaymentDto(createdRepayment);
    }

    public RepaymentDto getRepaymentById(final Long repaymentID) throws RepaymentNotFoundException {
        Repayment repayment = repaymentRepository
                .findById(repaymentID)
                .orElseThrow(RepaymentNotFoundException::new);

        return repaymentMapper.mapToRepaymentDto(repayment);
    }

    public List<RepaymentDto> getAllRepayments() {
        List<Repayment> retrievedRepaymentList = repaymentRepository.findAll();

        return repaymentMapper.mapToRepaymentDtoList(retrievedRepaymentList);
    }

    public RepaymentDto updateRepayment(RepaymentDto repaymentDto) throws RepaymentNotFoundException {
        Repayment repayment = repaymentRepository
                .findById(repaymentDto.getRepaymentId())
                .orElseThrow(RepaymentNotFoundException::new);

        repayment.setRepaymentAmount(repaymentDto.getRepaymentAmount());
        repayment.setRepaymentDate(repaymentDto.getRepaymentDate());

        Repayment updatedRepayment = repaymentRepository.saveAndFlush(repayment);

        return repaymentMapper.mapToRepaymentDto(updatedRepayment);
    }

    public void deleteRepaymentById(final Long repaymentId) throws RepaymentNotFoundException {
        if (repaymentRepository.findById(repaymentId).isPresent()) {
            repaymentRepository.deleteById(repaymentId);
        } else {
            throw new RepaymentNotFoundException();
        }
    }
}