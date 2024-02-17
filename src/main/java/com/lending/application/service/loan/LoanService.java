package com.lending.application.service.loan;

import com.lending.application.domain.loan.Loan;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    public Loan saveLoan(final Loan loan) {
        return loanRepository.saveAndFlush(loan);
    }

    public Loan getLoanById(final Long loanId) throws LoanNotFoundException {
        return loanRepository.findById(loanId).orElseThrow(LoanNotFoundException::new);
    }

    public List<Loan> getAllLoan() {
        return loanRepository.findAll();
    }

    public void deleteLoanById(final Long loanId) throws LoanNotFoundException {
        if (loanRepository.findById(loanId).isPresent()) {
            loanRepository.deleteById(loanId);
        } else {
            throw new LoanNotFoundException();
        }
    }
}