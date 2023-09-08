package com.lending.application.service.loan;

import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.mapper.LoanMapper;
import com.lending.application.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    public LoanDto createLoan(final LoanDto loanDto) {
        Loan loan = loanMapper.mapToLoan(loanDto);

        Loan createdLoan = loanRepository.saveAndFlush(loan);

        return loanMapper.mapToLoanDto(createdLoan);
    }

    public LoanDto getLoanById(final Long loanId) throws LoanNotFoundException {
        Loan retrievedLoan = loanRepository.findById(loanId).orElseThrow(LoanNotFoundException::new);

        return loanMapper.mapToLoanDto(retrievedLoan);
    }

    public List<LoanDto> getAllLoan() {
        List<Loan> loanList = loanRepository.findAll();

        return loanMapper.mapToLoanDtoList(loanList);
    }

    public LoanDto updateLoan(final LoanDto loanDto) throws LoanNotFoundException {
        Loan retrievedLoan = loanRepository.findById(loanDto.getLoanId()).orElseThrow(LoanNotFoundException::new);

        retrievedLoan.setLoanAmount(loanDto.getLoanAmount());
        retrievedLoan.setInterest(loanDto.getInterest());
        retrievedLoan.setLoanStartDate(loanDto.getLoanStartDate());
        retrievedLoan.setRepaymentPeriod(loanDto.getRepaymentPeriod());

        Loan updatedLoan = loanRepository.saveAndFlush(retrievedLoan);

        return loanMapper.mapToLoanDto(updatedLoan);
    }

    public void deleteLoanById(final Long loanId) throws LoanNotFoundException {
        if (loanRepository.findById(loanId).isPresent()) {
            loanRepository.deleteById(loanId);
        } else {
            throw new LoanNotFoundException();
        }
    }
}