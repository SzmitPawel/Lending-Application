package com.lending.application.relations;

import com.lending.application.domain.Loan;
import com.lending.application.domain.Repayment;
import com.lending.application.repository.LoanRepository;
import com.lending.application.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class LoanToRepaymentRelationsTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    RepaymentRepository repaymentRepository;

    @Test
    void createLoanWithTwoRepayments_shouldReturn1LoanAnd2Repayments() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023,01,01),
                22
        );

        Repayment repayment1 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,01,01)
        );
        Repayment repayment2 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,02,01)
        );

        loan.getRepaymentList().addAll(List.of(repayment1,repayment2));
        repayment1.setLoan(loan);
        repayment2.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // when & then
        assertEquals(2, loanRepository.findAll()
                .get(0)
                .getRepaymentList()
                .size());
    }

    @Test
    void deleteRepayment_shouldReturn1LoanAnd1repayment() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023,01,01),
                22
        );

        Repayment repayment1 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,01,01)
        );
        Repayment repayment2 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,02,01)
        );

        loan.getRepaymentList().addAll(List.of(repayment1,repayment2));
        repayment1.setLoan(loan);
        repayment2.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // when
        loan = loanRepository.findAll().get(0);
        Repayment repayment = loan.getRepaymentList().get(0);
        loan.getRepaymentList().remove(repayment);
        repaymentRepository.delete(repayment);
        loanRepository.saveAndFlush(loan);

        // then
        assertEquals(1, loanRepository.findAll()
                .get(0)
                .getRepaymentList()
                .size());
    }

    @Test
    void deleteLoanAndRepayments_shouldDeleteLoanWithAllRepayments() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023,01,01),
                22
        );

        Repayment repayment1 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,01,01)
        );
        Repayment repayment2 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,02,01)
        );

        loan.getRepaymentList().addAll(List.of(repayment1,repayment2));
        repayment1.setLoan(loan);
        repayment2.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // when
        loanRepository.deleteAll();

        // then
        assertEquals(0, loanRepository.count());
        assertEquals(0, repaymentRepository.count());
    }

    @Test
    void updateRepayment_shouldReturnUpdatedData() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023, 01, 01),
                22
        );

        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023, 01, 01)
        );

        loan.getRepaymentList().add(repayment);
        repayment.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // when
        repayment.setRepaymentDate(LocalDate.now());
        repayment.setRepaymentAmount(new BigDecimal(200.00));
        repaymentRepository.saveAndFlush(repayment);

        // then
        assertEquals(new BigDecimal(200.00), loanRepository.findAll()
                .get(0)
                .getRepaymentList()
                .get(0)
                .getRepaymentAmount());
        assertEquals(LocalDate.now(), loanRepository.findAll()
                .get(0)
                .getRepaymentList()
                .get(0)
                .getRepaymentDate());
    }

    @Test
    void readRepaymentFromLoan_shouldReturnAllData() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.of(2023, 01, 01),
                22
        );

        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023, 01, 01)
        );

        loan.getRepaymentList().add(repayment);
        repayment.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // then & then
        assertEquals(new BigDecimal(100.00), loanRepository.findAll()
                .get(0)
                .getRepaymentList()
                .get(0)
                .getRepaymentAmount());
        assertEquals(LocalDate.of(2023,01,01), loanRepository.findAll()
                .get(0)
                .getRepaymentList()
                .get(0)
                .getRepaymentDate());
    }
}