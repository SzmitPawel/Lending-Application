package com.lending.application.mapper;

import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanMapperTest {
    @Autowired
    LoanMapper loanMapper;

    private Loan prepareLoan() {
        return new Loan(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                22.00F,
                LocalDate.now(),
                5);
    }

    private LoanDto prepareLoanDto() {
        return new LoanDto(
                1L,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                22.00F,
                LocalDate.now(),
                5
        );
    }

    private List<Loan> prepareLoanList() {
        Loan loan1 = new Loan(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                22.00F,
                LocalDate.now(),
                5
        );
        loan1.setLoanId(1L);

        Loan loan2 = new Loan(
                BigDecimal.valueOf(2000),
                BigDecimal.valueOf(500),
                33.00F,
                LocalDate.now(),
                6
        );
        loan2.setLoanId(2L);

        return List.of(loan1,loan2);
    }

    private List<LoanDto> prepareLoanDtoList() {
        LoanDto loanDto1 = new LoanDto(
                1L,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                22.00F,
                LocalDate.now(),
                1
        );
        LoanDto loanDto2 = new LoanDto(
                2L,
                BigDecimal.valueOf(2000),
                BigDecimal.valueOf(500),
                33.00F,
                LocalDate.now(),
                2
        );

        return List.of(loanDto1,loanDto2);
    }

    @Test
    void testMapToLoanDto() {
        // given
        Loan loan = prepareLoan();
        loan.setLoanId(1L);

        // when
        LoanDto retrievedLoanDto = loanMapper.mapToLoanDto(loan);

        // then
        assertEquals(1L, retrievedLoanDto.getLoanId());
        assertEquals(new BigDecimal(1000), retrievedLoanDto.getLoanAmount());
        assertEquals(new BigDecimal(200), retrievedLoanDto.getMonthlyPayment());
        assertEquals(22.00F, retrievedLoanDto.getInterest());
        assertEquals(LocalDate.now(),retrievedLoanDto.getLoanStartDate());
        assertEquals(5, retrievedLoanDto.getRepaymentPeriod());
    }

    @Test
    void testMapToLoan() {
        // given
        LoanDto loanDto = prepareLoanDto();

        // when
        Loan retrievedLoan = loanMapper.mapToLoan(loanDto);

        // then
        assertEquals(1L, retrievedLoan.getLoanId());
        assertEquals(new BigDecimal(1000), retrievedLoan.getLoanAmount());
        assertEquals(new BigDecimal(200), retrievedLoan.getMonthlyPayment());
        assertEquals(22.00F, retrievedLoan.getInterest());
        assertEquals(LocalDate.now(),retrievedLoan.getLoanStartDate());
        assertEquals(5, retrievedLoan.getRepaymentPeriod());
    }

    @Test
    void testMapToLoanDtoList() {
        // given
        List<Loan> loanList = prepareLoanList();

        // when
        List<LoanDto> retrievedLoanDtoList = loanMapper.mapToLoanDtoList(loanList);

        // then
        assertEquals(2, retrievedLoanDtoList.size());
        assertEquals(1L, retrievedLoanDtoList.get(0).getLoanId());
        assertEquals(new BigDecimal(1000), retrievedLoanDtoList.get(0).getLoanAmount());
        assertEquals(new BigDecimal(200), retrievedLoanDtoList.get(0).getMonthlyPayment());
        assertEquals(22.00F, retrievedLoanDtoList.get(0).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanDtoList.get(0).getLoanStartDate());
        assertEquals(5, retrievedLoanDtoList.get(0).getRepaymentPeriod());
        assertEquals(2L, retrievedLoanDtoList.get(1).getLoanId());
        assertEquals(new BigDecimal(2000), retrievedLoanDtoList.get(1).getLoanAmount());
        assertEquals(new BigDecimal(500), retrievedLoanDtoList.get(1).getMonthlyPayment());
        assertEquals(33.00F, retrievedLoanDtoList.get(1).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanDtoList.get(1).getLoanStartDate());
        assertEquals(6, retrievedLoanDtoList.get(1).getRepaymentPeriod());
    }

    @Test
    void testMapToLoanList() {
        // given
        List<LoanDto> loanDtoList = prepareLoanDtoList();

        // when
        List<Loan> retrievedLoanList = loanMapper.mapToLoanList(loanDtoList);

        // then
        assertEquals(2, retrievedLoanList.size());
        assertEquals(1L, retrievedLoanList.get(0).getLoanId());
        assertEquals(new BigDecimal(1000), retrievedLoanList.get(0).getLoanAmount());
        assertEquals(new BigDecimal(200), retrievedLoanList.get(0).getMonthlyPayment());
        assertEquals(22.00F, retrievedLoanList.get(0).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanList.get(0).getLoanStartDate());
        assertEquals(1, retrievedLoanList.get(0).getRepaymentPeriod());
        assertEquals(2L, retrievedLoanList.get(1).getLoanId());
        assertEquals(new BigDecimal(2000), retrievedLoanList.get(1).getLoanAmount());
        assertEquals(new BigDecimal(500), retrievedLoanList.get(1).getMonthlyPayment());
        assertEquals(33.00F, retrievedLoanList.get(1).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanList.get(1).getLoanStartDate());
        assertEquals(2, retrievedLoanList.get(1).getRepaymentPeriod());
    }
}