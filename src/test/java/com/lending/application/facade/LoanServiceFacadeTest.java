package com.lending.application.facade;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.loan.Loan;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InvalidLoanAmountOfCreditException;
import com.lending.application.exception.InvalidLoanMonthsException;
import com.lending.application.exception.LowCreditRatingException;
import com.lending.application.mapper.loan.LoanResponseMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceFacadeTest {
    @InjectMocks
    private LoanServiceFacade loanServiceFacade;
    @Mock
    private LoanService loanService;
    @Mock
    private LoanResponseMapper loanResponseMapper;
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

    private LoanCalculationDto prepareLoanCalculationDTO() {
        LoanCalculationDto loanCalculationDto = new LoanCalculationDto();
        loanCalculationDto.setAmountOfCredit(BigDecimal.valueOf(1000.00));
        loanCalculationDto.setInterestRate(BigDecimal.valueOf(5.75));
        loanCalculationDto.setNumberOfMonths(12);
        loanCalculationDto.setMonthlyPayment(BigDecimal.valueOf(150.00));

        return loanCalculationDto;
    }

    private Loan prepareLoan() {
        Loan loan = new Loan();
        loan.setLoanId(1L);
        loan.setLoanAmount(BigDecimal.valueOf(1000.00));
        loan.setMonthlyPayment(BigDecimal.valueOf(150.00));
        loan.setInterest(5.75F);
        loan.setLoanStartDate(LocalDate.now());
        loan.setRepaymentPeriod(12);

        return loan;
    }

    @Test
    void create_new_loan_should_return_loan_response_dto() throws
            InvalidLoanAmountOfCreditException,
            IOException,
            InvalidLoanMonthsException,
            ClientNotFoundException,
            LowCreditRatingException {

        // given
        int months = 12;
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);

        Client client = prepareClient();
        when(clientService.getClientById(anyLong())).thenReturn(client);

        LoanCalculationDto loanCalculationDto = prepareLoanCalculationDTO();
        when(loanCalculatorService.calculateLoan(any(BigDecimal.class), anyInt())).thenReturn(loanCalculationDto);

        Loan loan = prepareLoan();
        when(loanService.saveLoan(any(Loan.class))).thenReturn(loan);

        // when
        loanServiceFacade.createNewLoan(client.getClientId(), amountOfCredit, months);

        // then
        verify(loanResponseMapper, times(1)).mapToLoanDTO(any(Loan.class));
        verify(loanService, times(1)).saveLoan(any(Loan.class));

        assertEquals(1, client.getLoanList().size());

        assertEquals(loanCalculationDto.getAmountOfCredit(), client.getLoanList().get(0).getLoanAmount());
        assertEquals(loanCalculationDto.getNumberOfMonths(), client.getLoanList().get(0).getRepaymentPeriod());
        assertEquals(loanCalculationDto.getInterestRate(), new BigDecimal(client.getLoanList().get(0).getInterest()));
        assertEquals(loanCalculationDto.getMonthlyPayment(), client.getLoanList().get(0).getMonthlyPayment());
        assertEquals(LocalDate.now(), client.getLoanList().get(0).getLoanStartDate());
    }

    @Test
    void create_new_loan_should_throw_low_credit_rating_exception_if_rating_is_five()
            throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);
        int months = 12;

        Client client = prepareClient();
        client.getCreditRating().setCreditRating(CreditRatingEnum.FIVE);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when & then
        assertThrows(LowCreditRatingException.class, () -> loanServiceFacade
                .createNewLoan(clientId, amountOfCredit, months));
    }

    @Test
    void create_new_loan_should_throw_low_credit_rating_exception_if_rating_is_four()
            throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);
        int months = 12;

        Client client = prepareClient();
        client.getCreditRating().setCreditRating(CreditRatingEnum.FOUR);

        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when & then
        assertThrows(LowCreditRatingException.class, () -> loanServiceFacade
                .createNewLoan(clientId, amountOfCredit, months));
    }
}