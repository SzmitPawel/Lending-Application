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
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private LoanMapper loanMapper;

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
        LoanDto loanDto = new LoanDto();
        Loan loan = new Loan();

        when(loanMapper.mapToLoan(loanDto)).thenReturn(loan);
        when(loanRepository.saveAndFlush(loan)).thenReturn(loan);
        when(loanMapper.mapToLoanDto(loan)).thenReturn(loanDto);

        // when
        LoanDto retrievedLoanDto = loanService.createLoan(loanDto);

        // then
        verify(loanMapper,times(1)).mapToLoan(any(LoanDto.class));
        verify(loanRepository,times(1)).saveAndFlush(any(Loan.class));
        verify(loanMapper,times(1)).mapToLoanDto(any(Loan.class));

        assertNotNull(retrievedLoanDto);
    }

    @Test
    void testGetLoanById() throws LoanNotFoundException {
        // given
        Loan loan = new Loan();
        LoanDto loanDto = new LoanDto();

        when(loanRepository.findById(any())).thenReturn(Optional.of(loan));
        when(loanMapper.mapToLoanDto(loan)).thenReturn(loanDto);

        // when
        LoanDto retrievedLoanDto = loanService.getLoanById(1L);

        // then
        verify(loanRepository,times(1)).findById(any());
        verify(loanMapper,times(1)).mapToLoanDto(any(Loan.class));

        assertNotNull(retrievedLoanDto);
    }

    @Test
    void testGetAllLoan() {
        // given
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();

        List<Loan> loanList = List.of(loan1,loan2);

        LoanDto loanDto1 = new LoanDto();
        LoanDto loanDto2 = new LoanDto();

        List<LoanDto> loanDtoList = List.of(loanDto1,loanDto2);

        when(loanRepository.findAll()).thenReturn(loanList);
        when(loanMapper.mapToLoanDtoList(loanList)).thenReturn(loanDtoList);

        // when
        List<LoanDto> retrievedLoanDtoList = loanService.getAllLoan();

        // then
        verify(loanRepository,times(1)).findAll();
        verify(loanMapper,times(1)).mapToLoanDtoList(loanList);

        assertNotNull(retrievedLoanDtoList);
    }

    @Test
    void testUpdateLoan() throws LoanNotFoundException {
        // given
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanId(1L);
        Loan loan = new Loan();

        when(loanRepository.findById(1l)).thenReturn(Optional.of(loan));
        when(loanRepository.saveAndFlush(loan)).thenReturn(loan);
        when(loanMapper.mapToLoanDto(loan)).thenReturn(loanDto);

        // when
        LoanDto retrievedLoanDto = loanService.updateLoan(loanDto);

        // then
        verify(loanRepository,times(1)).findById(any());
        verify(loanRepository,times(1)).saveAndFlush(any(Loan.class));
        verify(loanMapper,times(1)).mapToLoanDto(any(Loan.class));

        assertNotNull(retrievedLoanDto);
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