package com.lending.application.domain;

import com.lending.application.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoanTest {

    @Autowired
    LoanRepository loanRepository;

    @Test
    void createLoanWithoutLoanAmountInterestLoanStartRepaymentPeriod_shouldReturnDataIntegrityViolationException() {
        // given
        Loan loan = new Loan();

        // when & then
        assertThrows(DataIntegrityViolationException.class, ()-> loanRepository.saveAndFlush(loan));
    }

    @Test
    void createLoan_shouldReturnLoan() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000),
                5.0F,
                LocalDate.now(),
                22
        );
        loanRepository.saveAndFlush(loan);

        // then
        Loan retrievedLoan = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNotNull(retrievedLoan);
        assertEquals(1, loanRepository.count());
    }

    @Test
    void deleteLoan_shouldReturn0() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.now(),
                22
        );
        loanRepository.saveAndFlush(loan);

        // when
        loanRepository.delete(loan);

        Loan retrievedLoanAfterDelete = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNull(retrievedLoanAfterDelete);
        assertEquals(0, loanRepository.count());
    }

    @Test
    void updateLoan_shouldReturnUpdatedData() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023,01,01),
                22
        );
        loanRepository.saveAndFlush(loan);

        // when
        loan.setLoanAmount(new BigDecimal(3000.00));
        loan.setLoanStartDate(LocalDate.now());
        loan.setInterest(2.0F);
        loan.setRepaymentPeriod(10);
        loanRepository.saveAndFlush(loan);

        Loan retrievedLoanAfterUpdate = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNotNull(retrievedLoanAfterUpdate);
        assertEquals(new BigDecimal(3000.00), retrievedLoanAfterUpdate.getLoanAmount());
        assertEquals(2.0F, retrievedLoanAfterUpdate.getInterest());
        assertEquals(LocalDate.now(), retrievedLoanAfterUpdate.getLoanStartDate());
        assertEquals(10, retrievedLoanAfterUpdate.getRepaymentPeriod());
    }

    @Test
    void readLoan_shouldReturnAllData() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023,01,01),
                22
        );
        loanRepository.saveAndFlush(loan);

        // when
        Loan retrievedLoan = loanRepository.findById(loan.getLoanId()).orElse(null);

        // when & then
        assertNotNull(retrievedLoan);
        assertEquals(new BigDecimal(1000.00), retrievedLoan.getLoanAmount());
        assertEquals(5.0F, retrievedLoan.getInterest());
        assertEquals(LocalDate.of(2023,01,01), retrievedLoan.getLoanStartDate());
        assertEquals(22, retrievedLoan.getRepaymentPeriod());
    }
}