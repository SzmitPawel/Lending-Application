package com.lending.application.service.loan;

import com.lending.application.exception.InvalidLoanAmountOfCreditException;
import com.lending.application.exception.InvalidLoanMonthsException;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import com.lending.application.webScraping.WebScrapingStrategy;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@NoArgsConstructor
public class LoanCalculatorService {
    private WebScrapingStrategy webScrapingStrategy;

    public void setWebScrapingStrategy(WebScrapingStrategy webScrapingStrategy) {
        this.webScrapingStrategy = webScrapingStrategy;
    }

    public LoanCalculationDto calculateLoan(
            final BigDecimal amountOfCredit,
            final int months) throws IOException, InvalidLoanMonthsException, InvalidLoanAmountOfCreditException {

        BigDecimal yearInterest = getYearlyInterestFromSource();
        BigDecimal monthlyInterest = calculateMonthlyInterestRate(yearInterest);
        BigDecimal monthlyPayment = calculateMonthlyPayment(monthlyInterest,amountOfCredit,months);

        return prepareLoanCalculationDto(amountOfCredit,yearInterest,monthlyPayment,months);
    }

    private BigDecimal calculateMonthlyInterestRate(final BigDecimal yearInterest) {
        int year = 12;
        int percentageScale = 100;

        BigDecimal interestRate = yearInterest.divide(BigDecimal.valueOf(year), MathContext.DECIMAL64);

        return interestRate.divide(BigDecimal.valueOf(percentageScale));
    }

    private BigDecimal calculateMonthlyPayment(
            final BigDecimal monthlyInterest,
            final BigDecimal amountOfCredit,
            final int months
    ) throws InvalidLoanMonthsException, InvalidLoanAmountOfCreditException {

        if (amountOfCredit.compareTo(new BigDecimal("100000000000")) == 1) {
            throw new InvalidLoanAmountOfCreditException();
        }

        if (amountOfCredit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidLoanAmountOfCreditException();
        }

        if (months <= 0 || months > 1200) {
            throw new InvalidLoanMonthsException();
        }

        BigDecimal monthlyInterestAmount = amountOfCredit.multiply(monthlyInterest);
        BigDecimal powerExpressionResult
                = BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyInterest).pow(-months, MathContext.DECIMAL64));

        if (powerExpressionResult.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException();
        }

        return monthlyInterestAmount.divide(powerExpressionResult, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getYearlyInterestFromSource() throws IOException {
        return webScrapingStrategy.getInterestFromSource();
    }

    private LoanCalculationDto prepareLoanCalculationDto(
            final BigDecimal amountOfCredit,
            final BigDecimal yearInterest,
            final BigDecimal monthlyPayment,
            final int months
    ) {
       LoanCalculationDto loanCalculationDto = new LoanCalculationDto();
       loanCalculationDto.setAmountOfCredit(amountOfCredit);
       loanCalculationDto.setInterestRate(yearInterest);
       loanCalculationDto.setMonthlyPayment(monthlyPayment);
       loanCalculationDto.setNumberOfMonths(months);

       return loanCalculationDto;
    }
}