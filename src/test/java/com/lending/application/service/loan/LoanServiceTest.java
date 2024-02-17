package com.lending.application.service.loan;

import com.lending.application.domain.loan.Loan;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.repository.LoanRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoanServiceTest {
    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanRepository loanRepository;

    private Loan prepareLoan(final BigDecimal loanAmount, final BigDecimal monthlyPayment) {
        Loan loan = new Loan();
        loan.setLoanStartDate(LocalDate.now());
        loan.setLoanAmount(loanAmount);
        loan.setMonthlyPayment(monthlyPayment);
        loan.setRepaymentPeriod(10);
        loan.setInterest(3F);

        return loan;
    }

    @Test
    void testSaveLoan() {
        // given
        Loan loan = prepareLoan(BigDecimal.valueOf(2500.00), BigDecimal.valueOf(250.00));

        // when
        Loan retrievedLoan = loanService.saveLoan(loan);

        // then
        assertNotNull(retrievedLoan);
        assertEquals(loan.getLoanAmount(),retrievedLoan.getLoanAmount());
        assertEquals(loan.getLoanStartDate(), retrievedLoan.getLoanStartDate());
    }

    @Test
    void testGetLoanById_LoanNotFoundException() {
        // given
        Long loanId = 999L;

        // when & then
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(loanId));
    }

    @Test
    void testGetLoanById() throws LoanNotFoundException {
        // given
        Loan loan = loanRepository.saveAndFlush(prepareLoan(BigDecimal.valueOf(2500.00), BigDecimal.valueOf(250.00)));

        // when
        Loan retrievedLoan = loanService.getLoanById(loan.getLoanId());

        // then
        assertNotNull(retrievedLoan);
        assertEquals(loan.getLoanAmount(),retrievedLoan.getLoanAmount());
        assertEquals(loan.getLoanStartDate(), retrievedLoan.getLoanStartDate());
    }

    @Test
    void testGetAllLoan() {
        // given
        Loan loan1 = prepareLoan(BigDecimal.valueOf(1500.00), BigDecimal.valueOf(150.00));
        Loan loan2 = prepareLoan(BigDecimal.valueOf(2500.00), BigDecimal.valueOf(250.00));
        Loan loan3 = prepareLoan(BigDecimal.valueOf(3500.00), BigDecimal.valueOf(350.00));

        loanRepository.saveAndFlush(loan1);
        loanRepository.saveAndFlush(loan2);
        loanRepository.saveAndFlush(loan3);

        List<Loan> expectedLoanList = List.of(loan1,loan2,loan3);

        // when
        List<Loan> retrievedLoanList = loanService.getAllLoan();

        // then
        assertNotNull(retrievedLoanList);
        assertEquals(expectedLoanList.size(), retrievedLoanList.size());
        assertEquals(expectedLoanList.get(0).getLoanStartDate(),retrievedLoanList.get(0).getLoanStartDate());
        assertEquals(expectedLoanList.get(0).getLoanAmount(),retrievedLoanList.get(0).getLoanAmount());
        assertEquals(expectedLoanList.get(0).getInterest(),retrievedLoanList.get(0).getInterest());
        assertEquals(expectedLoanList.get(0).getMonthlyPayment(),retrievedLoanList.get(0).getMonthlyPayment());
        assertEquals(expectedLoanList.get(0).getInterest(),retrievedLoanList.get(0).getInterest());

        assertEquals(expectedLoanList.get(1).getLoanStartDate(),retrievedLoanList.get(1).getLoanStartDate());
        assertEquals(expectedLoanList.get(1).getLoanAmount(),retrievedLoanList.get(1).getLoanAmount());
        assertEquals(expectedLoanList.get(1).getInterest(),retrievedLoanList.get(1).getInterest());
        assertEquals(expectedLoanList.get(1).getMonthlyPayment(),retrievedLoanList.get(1).getMonthlyPayment());
        assertEquals(expectedLoanList.get(1).getInterest(),retrievedLoanList.get(1).getInterest());

        assertEquals(expectedLoanList.get(2).getLoanStartDate(),retrievedLoanList.get(2).getLoanStartDate());
        assertEquals(expectedLoanList.get(2).getLoanAmount(),retrievedLoanList.get(2).getLoanAmount());
        assertEquals(expectedLoanList.get(2).getInterest(),retrievedLoanList.get(2).getInterest());
        assertEquals(expectedLoanList.get(2).getMonthlyPayment(),retrievedLoanList.get(2).getMonthlyPayment());
        assertEquals(expectedLoanList.get(2).getInterest(),retrievedLoanList.get(2).getInterest());
    }

    @Test
    void testDeleteLoanById_LoanNotFoundException() {
        // given
        Long loanId = 999L;

        // when & then
        assertThrows(LoanNotFoundException.class, () -> loanService.deleteLoanById(loanId));
    }

    @Test
    void testDeleteLoanById() throws LoanNotFoundException {
        // given
        Loan loan = loanRepository.saveAndFlush(prepareLoan(BigDecimal.valueOf(2500.00), BigDecimal.valueOf(250.00)));

        // when
        loanService.deleteLoanById(loan.getLoanId());

        // then
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(loan.getLoanId()));
    }
}