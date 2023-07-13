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

    @Test
    void testMapToLoanDto() {
        // given
        Loan loan = new Loan(
                new BigDecimal(1),
                22.00F,
                LocalDate.now(),
                5);
        loan.setLoanId(1L);

        // when
        LoanDto retrievedLoanDto = loanMapper.mapToLoanDto(loan);

        // then
        assertEquals(1L, retrievedLoanDto.getLoanId());
        assertEquals(new BigDecimal(1), retrievedLoanDto.getLoanAmount());
        assertEquals(22.00F, retrievedLoanDto.getInterest());
        assertEquals(LocalDate.now(),retrievedLoanDto.getLoanStartDate());
        assertEquals(5, retrievedLoanDto.getRepaymentPeriod());
    }

    @Test
    void testMapToLoan() {
        // given
        LoanDto loanDto = new LoanDto(
                1L,
                new BigDecimal(1),
                22.00F,
                LocalDate.now(),
                5
        );

        // when
        Loan retrievedLoan = loanMapper.mapToLoan(loanDto);

        // then
        assertEquals(1L, retrievedLoan.getLoanId());
        assertEquals(new BigDecimal(1), retrievedLoan.getLoanAmount());
        assertEquals(22.00F, retrievedLoan.getInterest());
        assertEquals(LocalDate.now(),retrievedLoan.getLoanStartDate());
        assertEquals(5, retrievedLoan.getRepaymentPeriod());
    }

    @Test
    void testMapToLoanDtoList() {
        // given
        Loan loan1 = new Loan(
                new BigDecimal(1),
                22.00F,
                LocalDate.now(),
                5
        );
        loan1.setLoanId(1L);

        Loan loan2 = new Loan(
                new BigDecimal(2),
                33.00F,
                LocalDate.now(),
                6
        );
        loan2.setLoanId(2L);

        List<Loan> loanList = List.of(loan1,loan2);

        // when
        List<LoanDto> retrievedLoanDtoList = loanMapper.mapToLoanDtoList(loanList);

        // then
        assertEquals(2, retrievedLoanDtoList.size());
        assertEquals(1L, retrievedLoanDtoList.get(0).getLoanId());
        assertEquals(new BigDecimal(1), retrievedLoanDtoList.get(0).getLoanAmount());
        assertEquals(22.00F, retrievedLoanDtoList.get(0).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanDtoList.get(0).getLoanStartDate());
        assertEquals(5, retrievedLoanDtoList.get(0).getRepaymentPeriod());
        assertEquals(2L, retrievedLoanDtoList.get(1).getLoanId());
        assertEquals(new BigDecimal(2), retrievedLoanDtoList.get(1).getLoanAmount());
        assertEquals(33.00F, retrievedLoanDtoList.get(1).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanDtoList.get(1).getLoanStartDate());
        assertEquals(6, retrievedLoanDtoList.get(1).getRepaymentPeriod());
    }

    @Test
    void testMapToLoanList() {
        // given
        LoanDto loanDto1 = new LoanDto(
                1L,
                new BigDecimal(1),
                22.00F,
                LocalDate.now(),
                1
        );
        LoanDto loanDto2 = new LoanDto(
                2L,
                new BigDecimal(2),
                33.00F,
                LocalDate.now(),
                2
        );

        List<LoanDto> loanDtoList = List.of(loanDto1,loanDto2);

        // when
        List<Loan> retrievedLoanList = loanMapper.mapToLoanList(loanDtoList);

        // then
        assertEquals(2, retrievedLoanList.size());
        assertEquals(1L, retrievedLoanList.get(0).getLoanId());
        assertEquals(new BigDecimal(1), retrievedLoanList.get(0).getLoanAmount());
        assertEquals(22.00F, retrievedLoanList.get(0).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanList.get(0).getLoanStartDate());
        assertEquals(1, retrievedLoanList.get(0).getRepaymentPeriod());
        assertEquals(2L, retrievedLoanList.get(1).getLoanId());
        assertEquals(new BigDecimal(2), retrievedLoanList.get(1).getLoanAmount());
        assertEquals(33.00F, retrievedLoanList.get(1).getInterest());
        assertEquals(LocalDate.now(),retrievedLoanList.get(1).getLoanStartDate());
        assertEquals(2, retrievedLoanList.get(1).getRepaymentPeriod());
    }
}