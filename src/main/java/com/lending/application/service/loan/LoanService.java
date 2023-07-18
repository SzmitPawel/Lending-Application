package com.lending.application.service.loan;

import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.mapper.LoanMapper;
import com.lending.application.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LoanService {
    LoanRepository loanRepository;
    LoanMapper loanMapper;

    public void createLoan(final LoanDto loanDto) {
        loanRepository.saveAndFlush(loanMapper.mapToLoan(loanDto));
    }

    public LoanDto getLoanById(final Long loanId) throws LoanNotFoundException {
        Loan loan = loanRepository.findById(loanId).orElseThrow(LoanNotFoundException::new);

        return loanMapper.mapToLoanDto(loan);
    }

    public List<LoanDto> getAllLoan() {
        List<Loan> loanList = loanRepository.findAll();

        return loanMapper.mapToLoanDtoList(loanList);
    }

    public void updateLoan(final LoanDto loanDto) throws LoanNotFoundException {
        Loan loan = loanRepository.findById(loanDto.getLoanId()).orElseThrow(LoanNotFoundException::new);

        loan.setLoanAmount(loanDto.getLoanAmount());
        loan.setInterest(loanDto.getInterest());
        loan.setLoanStartDate(loanDto.getLoanStartDate());
        loan.setRepaymentPeriod(loanDto.getRepaymentPeriod());

        loanRepository.saveAndFlush(loan);
    }

    public void deleteLoanById(final Long loanId) throws LoanNotFoundException {
        if (loanRepository.findById(loanId).isPresent()) {
            loanRepository.deleteById(loanId);
        } else {
            throw new LoanNotFoundException();
        }
    }
}