package com.lending.application.service.loan;

import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.mapper.LoanMapper;
import com.lending.application.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
    @InjectMocks
    LoanService loanService;
    @Mock
    LoanRepository loanRepository;
    @Mock
    LoanMapper loanMapper;

    @Test
    void testGetLoanById_LoanNotFoundException() {
        // given
        when(loanRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(1L));
    }

    @Test
    void testDeleteLoanById_LoanNotFoundException() {
        // given
        when(loanRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(LoanNotFoundException.class, () -> loanService.deleteLoanById(1L));
    }

    @Test
    void testCreateLoan() {
        // given
        LoanDto loanDto = new LoanDto(
                1L,
                new BigDecimal(10),
                5.00F,
                LocalDate.now(),
                22
        );

        Loan loan = new Loan();

        when(loanMapper.mapToLoan(loanDto)).thenReturn(loan);

        // when
        loanService.createLoan(loanDto);

        // then
        verify(loanMapper, times(1)).mapToLoan(loanDto);
        verify(loanRepository, times(1)).saveAndFlush(loan);
    }

    @Test
    void testGetLoanById() throws LoanNotFoundException {
        // given
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanAmount(new BigDecimal(10));
        loan.setInterest(5.00F);
        loan.setLoanStartDate(LocalDate.now());
        loan.setRepaymentPeriod(24);

        when(loanRepository.findById(any())).thenReturn(Optional.of(loan));
        when(loanMapper.mapToLoanDto(loan)).thenCallRealMethod();

        // when
        LoanDto retrievedLoanDto = loanService.getLoanById(1L);

        // then
        verify(loanRepository, times(1)).findById(1L);
        verify(loanMapper, times(1)).mapToLoanDto(loan);
        assertEquals(1L, retrievedLoanDto.getLoanId());
        assertEquals(new BigDecimal(10), retrievedLoanDto.getLoanAmount());
        assertEquals(5.00F, retrievedLoanDto.getInterest());
        assertEquals(LocalDate.now(), retrievedLoanDto.getLoanStartDate());
        assertEquals(24, retrievedLoanDto.getRepaymentPeriod());
    }

    @Test
    void testGetAllLoan() {
        // given
        Loan loan1 = new Loan();
        loan1.setLoanId(1L);
        loan1.setLoanAmount(new BigDecimal(10));
        loan1.setInterest(5.00F);
        loan1.setLoanStartDate(LocalDate.now());
        loan1.setRepaymentPeriod(24);

        Loan loan2 = new Loan();
        loan2.setLoanId(2L);
        loan2.setLoanAmount(new BigDecimal(20));
        loan2.setInterest(5.50F);
        loan2.setLoanStartDate(LocalDate.now());
        loan2.setRepaymentPeriod(12);

        List<Loan> loanList = List.of(loan1,loan2);

        when(loanRepository.findAll()).thenReturn(loanList);
        when(loanMapper.mapToLoanDtoList(loanList)).thenCallRealMethod();

        // when
        List<LoanDto> retrievedLoanDtoList = loanService.getAllLoan();

        // then
        verify(loanRepository, times(1)).findAll();
        verify(loanMapper, times(1)).mapToLoanDtoList(loanList);
        assertEquals(2, retrievedLoanDtoList.size());
    }

    @Test
    void testUpdateLoan() throws LoanNotFoundException {
        // given
        LoanDto loanDto = new LoanDto(
                1L,
                new BigDecimal(10),
                5.00F,
                LocalDate.now(),
                24
        );

        Loan loan = new Loan();

        when(loanRepository.findById(any())).thenReturn(Optional.of(loan));

        // when
        loanService.updateLoan(loanDto);

        // then
        verify(loanRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).saveAndFlush(loan);
        assertEquals(new BigDecimal(10), loan.getLoanAmount());
        assertEquals(5.00F, loan.getInterest());
        assertEquals(LocalDate.now(), loan.getLoanStartDate());
        assertEquals(24, loan.getRepaymentPeriod());
    }

    @Test
    void testDeleteLoanById() throws LoanNotFoundException {
        // given
        Loan loan = new Loan();

        when(loanRepository.findById(any())).thenReturn(Optional.of(loan));

        // when
        loanService.deleteLoanById(1L);

        // then
        verify(loanRepository, times(1)).deleteById(1L);
    }
}