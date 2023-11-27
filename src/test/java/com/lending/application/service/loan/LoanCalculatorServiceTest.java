package com.lending.application.service.loan;

import com.lending.application.exception.InvalidLoanAmountOfCreditException;
import com.lending.application.exception.InvalidLoanMonthsException;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import com.lending.application.webScraping.BankierScraping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanCalculatorServiceTest {
    @InjectMocks
    LoanCalculatorService loanCalculatorService;
    @Mock
    BankierScraping bankierScraping;

    @Test
    void calculateLoan_shouldThrowIOException()
            throws IOException {
        // given
        when(bankierScraping.getInterestFromSource()).thenThrow(new IOException());

        // when & then
        assertThrows(IOException.class,
                () -> loanCalculatorService.calculateLoan(BigDecimal.valueOf(10000), 36));
    }

    @Test
    void calculateLoanInvalidNumberOfMonthsEquals0_shouldThrowInvalidLoanParametersException()
            throws IOException {
        // given
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000);
        int months = 0;
        BigDecimal interest = BigDecimal.valueOf(4.75);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when & then
        assertThrows(InvalidLoanMonthsException.class,
                () -> loanCalculatorService.calculateLoan(amountOfCredit,months));

    }

    @Test
    void calculateLoanInvalidNumberOfMonthsBelow0_shouldThrowInvalidLoanParametersException()
            throws IOException {
        // given
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000);
        int months = -1;
        BigDecimal interest = BigDecimal.valueOf(4.75);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when & then
        assertThrows(InvalidLoanMonthsException.class,
                () -> loanCalculatorService.calculateLoan(amountOfCredit,months));

    }

    @Test
    void calculateLoanInvalidNumberOfMonthsAbove1200_shouldThrowInvalidLoanParametersException()
            throws IOException {
        // given
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000);
        int months = 1201;
        BigDecimal interest = BigDecimal.valueOf(4.75);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when & then
        assertThrows(InvalidLoanMonthsException.class,
                () -> loanCalculatorService.calculateLoan(amountOfCredit,months));

    }

    @Test
    void calculateLoanInvalidAmountOfCreditEquals0_shouldThrowInvalidLoanAmountOfCreditException()
            throws IOException {
        // given
        BigDecimal amountOfCredit = BigDecimal.ZERO;
        int months = 36;
        BigDecimal interest = BigDecimal.valueOf(4.75);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when & then
        assertThrows(InvalidLoanAmountOfCreditException.class,
                () -> loanCalculatorService.calculateLoan(amountOfCredit,months));
    }

    @Test
    void calculateLoanInvalidAmountOfCreditBelow0_shouldThrowInvalidLoanParametersException() throws IOException {
        // given
        BigDecimal amountOfCredit = BigDecimal.valueOf(-1);
        int months = 36;
        BigDecimal interest = BigDecimal.valueOf(4.75);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when & then
        assertThrows(InvalidLoanAmountOfCreditException.class,
                () -> loanCalculatorService.calculateLoan(amountOfCredit,months));
    }

    @Test
    void calculateLoanInvalidAmountOfCreditAbove100Trillions_shouldThrowInvalidLoanAmountOfCreditException()
            throws IOException {
        // given
        BigDecimal amountOfCredit = new BigDecimal("100000000001");
        int months = 36;
        BigDecimal interest = BigDecimal.valueOf(4.75);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when & then
        assertThrows(InvalidLoanAmountOfCreditException.class,
                () -> loanCalculatorService.calculateLoan(amountOfCredit,months));
    }

    @Test
    void calculateLoanTheLowestLoanValueAndTheShortestPeriod()
            throws IOException, InvalidLoanMonthsException, InvalidLoanAmountOfCreditException {
        // given
        BigDecimal amountOfCredit = BigDecimal.ONE;
        int months = 1;
        BigDecimal interest = BigDecimal.valueOf(4.75);
        BigDecimal expectedMonthlyPayment = BigDecimal.ONE.setScale(2, RoundingMode.HALF_UP);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when
        LoanCalculationDto loanCalculationDto = loanCalculatorService.calculateLoan(amountOfCredit,months);

        // then
        assertNotNull(loanCalculationDto);
        assertEquals(amountOfCredit,loanCalculationDto.getAmountOfCredit());
        assertEquals(months,loanCalculationDto.getNumberOfMonths());
        assertEquals(interest,loanCalculationDto.getInterestRate());
        assertEquals(expectedMonthlyPayment,loanCalculationDto.getMonthlyPayment());
    }

    @Test
    void calculateLoanTheHighestLoanValueAndTheLongestPeriod()
            throws IOException,InvalidLoanMonthsException,InvalidLoanAmountOfCreditException {
        // given
        BigDecimal amountOfCredit = new BigDecimal("100000000000");
        int months = 1200;
        BigDecimal interest = BigDecimal.valueOf(4.75);
        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(399320679.76);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when
        LoanCalculationDto loanCalculationDto = loanCalculatorService.calculateLoan(amountOfCredit,months);

        // then
        assertNotNull(loanCalculationDto);
        assertEquals(amountOfCredit,loanCalculationDto.getAmountOfCredit());
        assertEquals(months,loanCalculationDto.getNumberOfMonths());
        assertEquals(interest,loanCalculationDto.getInterestRate());
        assertEquals(expectedMonthlyPayment,loanCalculationDto.getMonthlyPayment());
    }

    @Test
    void calculateLoan() throws IOException, InvalidLoanMonthsException, InvalidLoanAmountOfCreditException {
        // given
        BigDecimal amountOfCredit = BigDecimal.valueOf(10000);
        int months = 36;
        BigDecimal interest = BigDecimal.valueOf(5.75);

        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(303.09);

        when(bankierScraping.getInterestFromSource()).thenReturn(interest);

        // when
        LoanCalculationDto loanCalculationDto = loanCalculatorService.calculateLoan(amountOfCredit,months);

        // then
        assertNotNull(loanCalculationDto);
        assertEquals(amountOfCredit,loanCalculationDto.getAmountOfCredit());
        assertEquals(months,loanCalculationDto.getNumberOfMonths());
        assertEquals(interest,loanCalculationDto.getInterestRate());
        assertEquals(expectedMonthlyPayment,loanCalculationDto.getMonthlyPayment());
    }
}