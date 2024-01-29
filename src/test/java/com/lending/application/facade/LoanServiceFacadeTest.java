package com.lending.application.facade;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.Loan;
import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InvalidLoanAmountOfCreditException;
import com.lending.application.exception.InvalidLoanMonthsException;
import com.lending.application.exception.LowCreditRatingException;
import com.lending.application.mapper.LoanMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.loan.LoanCalculatorService;
import com.lending.application.service.loan.LoanService;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceFacadeTest {
    @InjectMocks
    private LoanServiceFacade loanServiceFacade;
    @Mock
    private LoanService loanService;
    @Mock
    private LoanMapper loanMapper;
    @Mock
    private ClientService clientService;
    @Mock
    private LoanCalculatorService loanCalculatorService;

    private CreditRating prepareCreditRating() {
        CreditRating creditRating = new CreditRating();
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setRatingId(1L);
        creditRating.setDateOfRating(LocalDate.now());

        return creditRating;
    }

    private Client prepareClient() {
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");
        client.setCreditRating(prepareCreditRating());

        return client;
    }

    @Test
    void testCreateNewLoan_shouldCreateNewLoan() throws
            InvalidLoanAmountOfCreditException,
            IOException,
            InvalidLoanMonthsException,
            ClientNotFoundException,
            LowCreditRatingException {

        // given
        Long clientId = 1L;
        Client client = prepareClient();
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);
        BigDecimal interest = BigDecimal.valueOf(5.75);
        BigDecimal monthlyPayment = new BigDecimal(150.00);
        int months = 12;

        LoanCalculationDto loanCalculationDto = new LoanCalculationDto();
        loanCalculationDto.setAmountOfCredit(amountOfCredit);
        loanCalculationDto.setInterestRate(interest);
        loanCalculationDto.setNumberOfMonths(months);
        loanCalculationDto.setMonthlyPayment(monthlyPayment);

        Loan loan = new Loan();
        loan.setLoanAmount(amountOfCredit);
        loan.setMonthlyPayment(monthlyPayment);
        loan.setInterest(interest.floatValue());
        loan.setLoanStartDate(LocalDate.now());
        loan.setRepaymentPeriod(months);

        when(clientService.getClientById(clientId)).thenReturn(client);
        when(loanCalculatorService.calculateLoan(amountOfCredit,months)).thenReturn(loanCalculationDto);
        when(loanService.saveLoan(any(Loan.class))).thenReturn(loan);
        when(loanMapper.mapToLoanDto(any(Loan.class))).thenCallRealMethod();

        // when
        LoanDto retrievedLoanDto = loanServiceFacade.createNewLoan(clientId,amountOfCredit,months);

        // then
        assertNotNull(retrievedLoanDto);
        assertEquals(amountOfCredit,retrievedLoanDto.getLoanAmount());
        assertEquals(interest.floatValue(),retrievedLoanDto.getInterest());
        assertEquals(months,retrievedLoanDto.getRepaymentPeriod());
        assertEquals(monthlyPayment,retrievedLoanDto.getMonthlyPayment());
    }

    @Test
    void testCreateNewLoan_shouldThrowLowCreditRatingExceptionTheWorstRating() throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);
        int months = 12;

        Client client = prepareClient();
        client.getCreditRating().setCreditRating(CreditRatingEnum.FIVE);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when & then
        assertThrows(LowCreditRatingException.class, () -> loanServiceFacade
                .createNewLoan(clientId,amountOfCredit,months));
    }

    @Test
    void testCreateNewLoan_shouldThrowLowCreditRatingExceptionMinimumBadRating() throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);
        int months = 12;

        Client client = prepareClient();
        client.getCreditRating().setCreditRating(CreditRatingEnum.FOUR);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when & then
        assertThrows(LowCreditRatingException.class, () -> loanServiceFacade
                .createNewLoan(clientId,amountOfCredit,months));
    }
}