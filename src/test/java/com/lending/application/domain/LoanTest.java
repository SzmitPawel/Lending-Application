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
        assertThrows(DataIntegrityViolationException.class, ()-> loanRepository.save(loan));
    }

    @Test
    void createLoan_shouldReturn1() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000),
                5.0F,
                LocalDate.now(),
                22
        );

        loanRepository.save(loan);

        // when & then
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

        loanRepository.save(loan);

        // when
        loanRepository.delete(loan);

        // then
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
        loanRepository.save(loan);

        // when
        loan.setLoanAmount(new BigDecimal(3000.00));
        loan.setLoanStartDate(LocalDate.now());
        loan.setInterest(2.0F);
        loan.setRepaymentPeriod(10);
        loanRepository.save(loan);

        // then
        assertEquals(new BigDecimal(3000.00), loanRepository
                        .findAll()
                        .get(0)
                        .getLoanAmount());
        assertEquals(2.0F, loanRepository
                .findAll()
                .get(0)
                .getInterest());
        assertEquals(LocalDate.now(), loanRepository
                .findAll()
                .get(0)
                .getLoanStartDate());
        assertEquals(10, loanRepository
                .findAll()
                .get(0)
                .getRepaymentPeriod());
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
        loanRepository.save(loan);

        // when & then
        assertEquals(new BigDecimal(1000.00), loanRepository
                .findAll()
                .get(0)
                .getLoanAmount());
        assertEquals(5.0F, loanRepository
                .findAll()
                .get(0)
                .getInterest());
        assertEquals(LocalDate.of(2023,01,01), loanRepository
                .findAll()
                .get(0)
                .getLoanStartDate());
        assertEquals(22, loanRepository
                .findAll()
                .get(0)
                .getRepaymentPeriod());
    }
}