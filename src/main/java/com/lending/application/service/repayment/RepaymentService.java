package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.exception.RepaymentNotFoundException;
import com.lending.application.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepaymentService {
    private final RepaymentRepository repaymentRepository;

    public Repayment saveRepayment(final Repayment repayment) {
        return repaymentRepository.saveAndFlush(repayment);
    }

    public Repayment getRepaymentById(final Long repaymentID) throws RepaymentNotFoundException {
        return repaymentRepository.findById(repaymentID).orElseThrow(RepaymentNotFoundException::new);
    }

    public List<Repayment> getAllRepayments() {
        return repaymentRepository.findAll();
    }

    public void deleteRepaymentById(final Long repaymentId) throws RepaymentNotFoundException {
        if (repaymentRepository.findById(repaymentId).isPresent()) {
            repaymentRepository.deleteById(repaymentId);
        } else {
            throw new RepaymentNotFoundException();
        }
    }
}