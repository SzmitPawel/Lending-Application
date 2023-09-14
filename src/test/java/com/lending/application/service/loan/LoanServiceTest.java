package com.lending.application.service.loan;

import com.lending.application.domain.Loan;
import com.lending.application.exception.LoanNotFoundException;
import com.lending.application.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {
    @InjectMocks
    private LoanService loanService;
    @Mock
    private LoanRepository loanRepository;

    @Test
    void testSaveLoan() {
        // given
        Loan loan = new Loan();

        when(loanRepository.saveAndFlush(any(Loan.class))).thenReturn(loan);

        // when
        Loan retrievedLoan = loanService.saveLoan(loan);

        // then
        verify(loanRepository,times(1)).saveAndFlush(any(Loan.class));

        assertNotNull(retrievedLoan);
    }

    @Test
    void testGetLoanById_LoanNotFoundException() {
        // given
        when(loanRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(LoanNotFoundException.class, () -> loanService.getLoanById(1L));
    }

    @Test
    void testGetLoanById() throws LoanNotFoundException {
        // given
        Loan loan = new Loan();

        when(loanRepository.findById(any())).thenReturn(Optional.of(loan));

        // when
        Loan retrievedLoan = loanService.getLoanById(1L);

        // then
        verify(loanRepository,times(1)).findById(any());

        assertNotNull(retrievedLoan);
    }

    @Test
    void testGetAllLoan() {
        // given
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();

        List<Loan> loanList = List.of(loan1,loan2);

        when(loanRepository.findAll()).thenReturn(loanList);

        // when
        List<Loan> retrievedLoanList = loanService.getAllLoan();

        // then
        verify(loanRepository,times(1)).findAll();

        assertNotNull(retrievedLoanList);
    }

    @Test
    void testDeleteLoanById_LoanNotFoundException() {
        // given
        when(loanRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(LoanNotFoundException.class, () -> loanService.deleteLoanById(1L));
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