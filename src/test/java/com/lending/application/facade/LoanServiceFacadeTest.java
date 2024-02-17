package com.lending.application.facade;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.loan.Loan;
import com.lending.application.domain.loan.LoanResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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
        Client client = prepareClient();
        LoanCalculationDto loanCalculationDto = prepareLoanCalculationDTO();
        Loan loan = prepareLoan();

        int months = 12;
        BigDecimal amountOfCredit = BigDecimal.valueOf(1000.00);

        when(clientService.getClientById(anyLong())).thenReturn(client);
        when(loanCalculatorService.calculateLoan(any(BigDecimal.class), anyInt())).thenReturn(loanCalculationDto);
        when(loanService.saveLoan(any(Loan.class))).thenReturn(loan);
        when(loanResponseMapper.mapToLoanDTO(any(Loan.class))).thenCallRealMethod();

        // when
        LoanResponseDTO retrievedLoanResponseDTO = loanServiceFacade
                .createNewLoan(client.getClientId(), amountOfCredit, months);

        // then
        assertNotNull(retrievedLoanResponseDTO);
        assertEquals(loan.getLoanId(), retrievedLoanResponseDTO.getLoanId());
        assertEquals(loan.getLoanAmount(), retrievedLoanResponseDTO.getLoanAmount());
        assertEquals(loan.getInterest(), retrievedLoanResponseDTO.getInterest());
        assertEquals(loan.getRepaymentPeriod(), retrievedLoanResponseDTO.getRepaymentPeriod());
        assertEquals(loan.getMonthlyPayment(), retrievedLoanResponseDTO.getMonthlyPayment());
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