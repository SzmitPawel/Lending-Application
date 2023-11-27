package com.lending.application.facade;

import com.lending.application.domain.Client;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.*;
import com.lending.application.mapper.LoanMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.loan.LoanCalculatorService;
import com.lending.application.service.loan.LoanService;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import com.lending.application.webScraping.BankierScraping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceFacade {
    private final LoanService loanService;
    private final LoanMapper loanMapper;
    private final ClientService clientService;
    private final LoanCalculatorService loanCalculatorService;
    private final BankierScraping bankierScraping;

    public LoanCalculationDto calculateLoan(final BigDecimal amountOfCredit, final int months)
            throws InvalidLoanAmountOfCreditException, IOException, InvalidLoanMonthsException {

        loanCalculatorService.setWebScrapingStrategy(bankierScraping);

        return loanCalculatorService.calculateLoan(amountOfCredit,months);
    }

    public LoanDto createNewLoan(final Long clientId, final BigDecimal amountOfCredit, final int months)
            throws ClientNotFoundException,
            LowCreditRatingException,
            InvalidLoanAmountOfCreditException,
            IOException,
            InvalidLoanMonthsException {

        loanCalculatorService.setWebScrapingStrategy(bankierScraping);
        Client client = clientService.getClientById(clientId);
        CreditRatingEnum rating = client.getCreditRating().getCreditRating();

        if (checkRating(rating)) {
            return createLoan(client, amountOfCredit, months);
        } else {
            throw new LowCreditRatingException();
        }
    }

    public List<LoanDto> getAllClientLoans(final Long clientId) throws ClientNotFoundException {
        Client client = clientService.getClientById(clientId);
        List<Loan> retrievedLoanList = client.getLoanList();

        return loanMapper.mapToLoanDtoList(retrievedLoanList);
    }

    public LoanDto getLoanById(final Long loanId) throws LoanNotFoundException {
        Loan loan = loanService.getLoanById(loanId);

        return loanMapper.mapToLoanDto(loan);
    }

    private boolean checkRating(CreditRatingEnum rating) {
        return rating == CreditRatingEnum.ONE || rating == CreditRatingEnum.TWO || rating == CreditRatingEnum.THREE;
    }

    private LoanDto createLoan(
            final Client client,
            final BigDecimal amountOfCredit,
            final int months) throws InvalidLoanAmountOfCreditException, IOException, InvalidLoanMonthsException {

        LoanCalculationDto loanCalculationDto = loanCalculatorService.calculateLoan(amountOfCredit,months);
        Loan loan = prepareLoan(loanCalculationDto);
        loan.setClient(client);

        return loanMapper.mapToLoanDto(loanService.saveLoan(loan));
    }

    private Loan prepareLoan(final LoanCalculationDto loanCalculationDto) {
        Loan loan = new Loan();
        loan.setLoanAmount(loanCalculationDto.getAmountOfCredit());
        loan.setMonthlyPayment(loanCalculationDto.getMonthlyPayment());
        loan.setInterest(loanCalculationDto.getInterestRate().floatValue());
        loan.setLoanStartDate(LocalDate.now());
        loan.setRepaymentPeriod(loanCalculationDto.getNumberOfMonths());

        return loan;
    }
}