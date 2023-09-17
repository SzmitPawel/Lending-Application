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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LoanToRepaymentRelationsTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    RepaymentRepository repaymentRepository;

    private Loan prepareLoan() {
        return new Loan(
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(200),
                5.0F,
                LocalDate.now(),
                22
        );
    }

    private Repayment prepareRepayment() {
        return new Repayment(
                BigDecimal.valueOf(100.00),
                LocalDate.of(2023, 01, 01));
    }

    private List<Repayment> prepareRepaymentList() {
        Repayment repayment1 = new Repayment(
                BigDecimal.valueOf(100.00),
                LocalDate.of(2023,01,01)
        );
        Repayment repayment2 = new Repayment(
                new BigDecimal(100.00),
                LocalDate.of(2023,02,01)
        );

        return List.of(repayment1,repayment2);
    }

    @Test
    void createLoanWithTwoRepayments_shouldReturn1LoanAnd2Repayments() {
        // given
        Loan loan = prepareLoan();
        loan.getRepaymentList().addAll(prepareRepaymentList());

        loan.getRepaymentList().stream().forEach(repayment -> repayment.setLoan(loan));
        loanRepository.saveAndFlush(loan);

        // when
        Loan retrievedLoan = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNotNull(retrievedLoan);
        assertEquals(2, retrievedLoan.getRepaymentList().size());
    }

    @Test
    void deleteRepayment_shouldReturn1LoanAnd1repayment() {
        // given
        Loan loan = prepareLoan();
        loan.getRepaymentList().addAll(prepareRepaymentList());

        loan.getRepaymentList().stream().forEach(repayment -> repayment.setLoan(loan));
        loanRepository.saveAndFlush(loan);

        // when
        Loan retrievedLoan = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        assertNotNull(retrievedLoan);

        repaymentRepository.delete(retrievedLoan.getRepaymentList().get(0));
        retrievedLoan.getRepaymentList().remove(0);
        loanRepository.saveAndFlush(loan);

        Loan retrievedLoanAfterDelete = loanRepository
                .findById(retrievedLoan.getLoanId())
                .orElse(null);

        // then
        assertNotNull(retrievedLoanAfterDelete);
        assertEquals(1, retrievedLoan.getRepaymentList().size());
    }

    @Test
    void deleteLoanAndRepayments_shouldDeleteLoanWithAllRepayments() {
        // given
        Loan loan = prepareLoan();
        loan.getRepaymentList().addAll(prepareRepaymentList());

        loan.getRepaymentList().stream().forEach(repayment -> repayment.setLoan(loan));
        loanRepository.saveAndFlush(loan);

        // when
        loanRepository.deleteAll();

        Loan retrievedLoanAfterDelete = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNull(retrievedLoanAfterDelete);
        assertEquals(0, loanRepository.count());
        assertEquals(0, repaymentRepository.count());
    }

    @Test
    void updateRepayment_shouldReturnUpdatedData() {
        // given
        Loan loan = prepareLoan();
        Repayment repayment = prepareRepayment();

        loan.getRepaymentList().add(repayment);
        repayment.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // when
        repayment.setRepaymentDate(LocalDate.now());
        repayment.setRepaymentAmount(BigDecimal.valueOf(200.00));
        repaymentRepository.saveAndFlush(repayment);

        Loan retrievedLoanAfterUpdate = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNotNull(retrievedLoanAfterUpdate);
        assertEquals(BigDecimal.valueOf(200.00), retrievedLoanAfterUpdate
                .getRepaymentList()
                .get(0)
                .getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedLoanAfterUpdate
                .getRepaymentList()
                .get(0)
                .getRepaymentDate());
    }

    @Test
    void readRepaymentFromLoan_shouldReturnAllData() {
        // given
        Loan loan = prepareLoan();
        Repayment repayment = prepareRepayment();

        loan.getRepaymentList().add(repayment);
        repayment.setLoan(loan);
        loanRepository.saveAndFlush(loan);

        // when
        Loan retrievedLoan = loanRepository
                .findById(loan.getLoanId())
                .orElse(null);

        // then
        assertNotNull(retrievedLoan);
        assertEquals(BigDecimal.valueOf(100.00), retrievedLoan
                .getRepaymentList()
                .get(0)
                .getRepaymentAmount());
        assertEquals(LocalDate.of(2023,01,01), retrievedLoan
                .getRepaymentList()
                .get(0)
                .getRepaymentDate());
    }
}